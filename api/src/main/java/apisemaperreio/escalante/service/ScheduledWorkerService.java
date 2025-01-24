package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apisemaperreio.escalante.dto.CountScheduledWorkerDTO;
import apisemaperreio.escalante.dto.DeleteScheduledWorkerDTO;
import apisemaperreio.escalante.dto.SaveScheduledWorkerDTO;
import apisemaperreio.escalante.dto.ScheduledWorkerDTO;
import apisemaperreio.escalante.dto.UpdateScheduledWorkerDTO;
import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.model.WorkerRole;
import apisemaperreio.escalante.repository.ScheduledWorkerRepository;
import apisemaperreio.escalante.repository.WorkerPriorityRepository;
import apisemaperreio.escalante.repository.WorkerRepository;
import apisemaperreio.escalante.repository.WorkerRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService extends BaseService {

    private final WorkerRepository workerRepository;
    private final ScheduledWorkerRepository scheduledRepository;
    private final WorkerRoleRepository roleRepository;
    private final WorkerPriorityRepository priorityRepository;

    // Metodo para checar se existe um tipo de escala de trabalho especifica
    // (ScheduleType) para um certo trabalhador ou cargo,
    // se houver retorna este tipo de escala, se não, retorna um Optional vazio.
    private Optional<ScheduleType> checkScheduleType(Worker worker) {
        var workerScheduleType = Optional.ofNullable(worker.getScheduleType());
        var positionScheduleType = Optional.ofNullable(worker.getPosition().getScheduleType());
        if (workerScheduleType.isEmpty() && positionScheduleType.isEmpty())
            return Optional.empty();
        if (workerScheduleType.isPresent() && positionScheduleType.isPresent())
            return workerScheduleType.get().getDaysOff() >= positionScheduleType.get().getDaysOff()
                    ? workerScheduleType
                    : positionScheduleType;
        return workerScheduleType.isPresent() ? workerScheduleType : positionScheduleType;
    }

    // Metodo para retornar o ultimo dia que o trabalhador foi escalado.
    private ScheduledWorker lastWork(Worker worker) {
        var workedDays = new ArrayList<>(worker.getScheduledWorkers());
        workedDays.sort(Comparator.comparing(ScheduledWorker::getDate).reversed());
        return workedDays.getFirst();
    }

    // Metodo para retornar um Optional com um trabalhador,
    // se ele estiver disponível para trabalhar em certa data,
    // ou um Optinal vazio caso não haja trabalhadores disponíveis.
    private Optional<Worker> selectWorker(LocalDate date, List<Worker> workers) {
        for (var worker : workers) {
            if (worker.getScheduledWorkers().isEmpty())
                return Optional.of(worker);
            var scheduleType = checkScheduleType(worker);
            var lastWork = lastWork(worker);
            if (scheduleType.isPresent()
                    && (scheduleType.get().getDaysOff() > lastWork.getRole().getScheduleType().getDaysOff())) {
                if (lastWork.getDate().plusDays(scheduleType.get().getDaysOff() + 1).isAfter(date))
                    continue;
                return Optional.of(worker);
            }
            if (lastWork.getDate().plusDays(lastWork.getRole().getScheduleType().getDaysOff() + 1).isAfter(date))
                continue;
            return Optional.of(worker);
        }
        return Optional.empty();
    }

    // Metodo para retornar os dias trabalhados por certo trabalhador,
    // em certa função, de acordo com a quantidade de dias determinada
    // pelo parametro daysWork, se a função do trabalhador for 'permanente'
    // ou 'auxiliar de linha', as funções serão alternadas entre si,
    // quando os dias de trabalho forem superiores a 1 dia.
    private List<ScheduledWorker> scheduledWorkersDay(Optional<Worker> worker, LocalDate date, List<WorkerRole> roles,
            WorkerRole role, Integer daysWork) {
        var scheduledWorkersDay = new ArrayList<ScheduledWorker>();
        var newDate = date;
        for (int i = 1; i <= daysWork; i++) {
            var scheduledWorker = ScheduledWorker.builder()
                    .date(newDate)
                    .worker(worker.orElseThrow())
                    .role(role)
                    .build();
            scheduledWorkersDay.add(scheduledWorker);
            if (i > 1 && i % 2 == 0) {
                if (role.getPriority() == 2)
                    scheduledWorkersDay.getLast().setRole(roles.get(2));
                else if (role.getPriority() == 3)
                    scheduledWorkersDay.getLast().setRole(roles.get(1));
            }
            newDate = newDate.plusDays(1);
        }
        return scheduledWorkersDay;
    }

    // Metodo para verificar se o motorista tambem pode ser o fiscal,
    // para isto ele deve recuperar da lista de trabalhadores escalados
    // o motorista e o chefe de linha que trabalharão em certa data,
    // verificar se algum deles existe, se o motorista não existir ou
    // o chefe de linha for mais antigo que o motorista,
    // será retornado um Optional vazio, caso contrario o motorista será retornado.
    private Optional<Worker> driverEqualsFiscal(LocalDate date) {
        var scheduledDriver = workerRepository.findByWorkedDateAndRolePriority(date, 1);
        var scheduledChefeLinha = workerRepository.findByWorkedDateAndRolePriority(date, 4);
        if (scheduledDriver.isEmpty())
            return Optional.empty();
        if (scheduledChefeLinha.isEmpty())
            return scheduledDriver;
        return scheduledDriver.get().getSeniority() < scheduledChefeLinha.get().getSeniority()
                ? scheduledDriver
                : Optional.empty();
    }

    // Retorna um trabalhador para as funções diferentes da função de motorista,
    // de acordo com a ordem de prioridade da função recebida por parametro.
    // a busca é realizada em duas etapas, primeiro buscando um trabalhador
    // que não pode exercer a função de motorista, caso não haja,
    // será buscado os podem exerce-la, se ainda assim não houver,
    // será retornado um Optional vazio.
    private Optional<Worker> selectNoDriver(LocalDate date, WorkerRole role) {
        var priorities = priorityRepository.findByRoleOrderByPriorityAsc(role);
        for (var priority : priorities) {
            var worker = selectWorker(date,
                    workerRepository.findAvailableWorkers(date, priority.getPosition().getId(), false));
            if (worker.isEmpty())
                continue;
            return worker;
        }
        for (var priority : priorities) {
            var worker = selectWorker(date,
                    workerRepository.findAvailableWorkers(date, priority.getPosition().getId(), true));
            if (worker.isEmpty())
                continue;
            return worker;
        }
        return Optional.empty();
    }

    // Metodo para salvar os trabalhadores escalados no banco de dados
    // Caso não haja trabalhador o metodo não fará nada.
    private void saveScheduledWorkers(Optional<Worker> worker, LocalDate date, List<WorkerRole> roles,
            WorkerRole role, Integer daysWork) {
        if (worker.isPresent())
            worker.get().getScheduledWorkers().addAll(scheduledWorkersDay(worker, date, roles, role, daysWork));
    }

    // Metodo responsável por escalar os trabalhadores, conforme as funções
    // recuperadas do banco de dados, ordenadas por prioridade, para certo
    // periodo de tempo e quantidade de dias trabalhados passados por parâmetro.
    // Há três lógicas diferentes para escalar os trabalhadores,
    // dependendo da função que estão sendo escalados:
    // 1 - Caso a função seja de motorista;
    // 2 - Caso do função de fiscal;
    // 3 - Caso de qualquer outra função.
    @Transactional
    public List<ScheduledWorkerDTO> scheduler(LocalDate startDate, LocalDate endDate, Integer daysWork) {
        if (scheduledRepository.existsInRangeDates(startDate, endDate))
            throw new IllegalArgumentException("There are workers scheduled on the dates provided");
        var roles = roleRepository.findAllByOrderByPriorityAsc();
        var currentDate = startDate;
        while (currentDate.compareTo(endDate) <= 0) {
            for (var role : roles) {
                Optional<Worker> worker = Optional.empty();
                if (role.getPriority() == 1) {
                    worker = selectWorker(currentDate, workerRepository.findAvailableDrivers(currentDate));
                    saveScheduledWorkers(worker, currentDate, roles, role, daysWork);
                    continue;
                }
                if (role.getPriority() == 5) {
                    var driverEqualsFiscal = driverEqualsFiscal(currentDate);
                    if (driverEqualsFiscal.isPresent()) {
                        var newChefeLinha = selectNoDriver(currentDate, roles.get(3));
                        if (newChefeLinha.isPresent()
                                && driverEqualsFiscal.get().getSeniority() < newChefeLinha.get().getSeniority()) {
                            worker = driverEqualsFiscal;
                            saveScheduledWorkers(newChefeLinha, currentDate, roles, roles.get(3), daysWork);
                            saveScheduledWorkers(worker, currentDate, roles, role, daysWork);
                            break;
                        }
                        worker = selectNoDriver(currentDate, role);
                        if (worker.isEmpty())
                            worker = driverEqualsFiscal;
                        saveScheduledWorkers(worker, currentDate, roles, role, daysWork);
                        break;
                    }
                }
                worker = selectNoDriver(currentDate, role);
                saveScheduledWorkers(worker, currentDate, roles, role, daysWork);
            }
            currentDate = currentDate.plusDays(daysWork);
        }
        return getAllScheduledWorkersRangeDate(startDate, endDate);
    }

    // Metodo para retornar um registro da escala de trabalho por Id.
    public Optional<ScheduledWorkerDTO> getScheduledWorkerById(Integer id) {
        return scheduledRepository.findById(id).map(this::toDto);
    }

    // Metodo para retornar os trabalhadores escalados em um certo dia.
    public List<ScheduledWorkerDTO> getScheduledWorkerByDate(LocalDate date) {
        return scheduledRepository.findByDate(date).stream().map(this::toDto).toList();
    }

    // Metodo para retornar os trabalhadores escalados em um certo periodo de tempo.
    public List<ScheduledWorkerDTO> getAllScheduledWorkersRangeDate(LocalDate startDate, LocalDate endDate) {
        return scheduledRepository.findByRangeDates(startDate, endDate).stream().map(this::toDto).toList();
    }

    // Metodo para retornar os dias que certo trabalhador trabalhou
    // em um certo intervalo de tempo
    public List<ScheduledWorkerDTO> getScheduledWorkerRangeDate(String workerRegistration, LocalDate startDate,
            LocalDate endDate) {
        var scheduledWorkers = scheduledRepository.findScheduledWorkerRangeDate(workerRegistration, startDate, endDate);
        return scheduledWorkers.stream().map(this::toDto).toList();
    }

    // Metodo para retornar quantos dias todos os trabalhadores trabalharam em um
    // certo periodo de tempo.
    public List<CountScheduledWorkerDTO> getAllWorkersCountDays(LocalDate startDate, LocalDate endDate) {
        var workersDays = workerRepository.findAllWorkersCountWorkedDays(startDate, endDate);
        if (workersDays.isEmpty())
            return List.of();
        return workersDays.stream().map(w -> CountScheduledWorkerDTO.builder()
                .worker(toSimpleDto((Worker) w[0]))
                .count((Long) w[1])
                .build()).toList();
    }

    // Metodo para retornar quantos dias um trabalhador trabalhou em um certo
    // periodo de tempo.
    public Optional<CountScheduledWorkerDTO> getWorkerCountDays(String workerRegistration, LocalDate startDate,
            LocalDate endDate) {
        var workerDays = workerRepository.findByWorkerCountWorkedDays(workerRegistration, startDate, endDate);
        if (workerDays.isEmpty())
            return Optional.empty();
        return Optional.of(CountScheduledWorkerDTO.builder()
                .worker(toSimpleDto((Worker) workerDays.getFirst()[0]))
                .count((Long) workerDays.getFirst()[1])
                .build());
    }

    // Metodo para retornar inconsistencias na escala de trabalho.
    public List<ScheduledWorkerDTO> getInconsistencies(LocalDate startDate, LocalDate endDate) {
        var scheduledWorkers = scheduledRepository.findInconsistencies(startDate, endDate);
        return scheduledWorkers.stream().map(this::toDto).toList();
    }

    // Metodo para adicionar um trabalhador a escala de trabalho manualmente.
    public ScheduledWorkerDTO saveScheduledWorker(SaveScheduledWorkerDTO saveScheduledWorkerDto) {
        var scheduledWorker = ScheduledWorker.builder()
                .date(LocalDate.parse(saveScheduledWorkerDto.workDate()))
                .worker(workerRepository.findByRegistration(saveScheduledWorkerDto.workerRegistration())
                        .orElseThrow(() -> new NoSuchElementException("No worker found")))
                .role(roleRepository.findByName(saveScheduledWorkerDto.workerRoleName())
                        .orElseThrow(() -> new NoSuchElementException("No role found")))
                .build();
        return toDto(scheduledRepository.save(scheduledWorker));
    }

    // Metodo para alterar um registro da escala de trabalho.
    public void updateScheduledWorker(Integer id, UpdateScheduledWorkerDTO updateScheduledWorkerDto) {
        var scheduledWorker = scheduledRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No scheduled worker found"));
        if (validadeUpdate(scheduledWorker.getDate().toString(), updateScheduledWorkerDto.workDate())) {
            scheduledWorker.setDate(LocalDate.parse(updateScheduledWorkerDto.workDate()));
        }
        if (validadeUpdate(scheduledWorker.getWorker().getRegistration(),
                updateScheduledWorkerDto.workerRegistration())) {
            scheduledWorker.setWorker(workerRepository.findByRegistration(updateScheduledWorkerDto.workerRegistration())
                    .orElseThrow(() -> new NoSuchElementException("No worker found")));
        }
        if (validadeUpdate(scheduledWorker.getRole().getName(), updateScheduledWorkerDto.workerRoleName())) {
            scheduledWorker.setRole(roleRepository.findByName(updateScheduledWorkerDto.workerRoleName())
                    .orElseThrow(() -> new NoSuchElementException("No role found")));
        }
        scheduledRepository.save(scheduledWorker);
    }

    // Metodo para permutar dois trabalhadores na escala de trabalho.
    public void swapTwoScheduledWorkers(Integer id1, Integer id2) {
        var scheduledWorker1 = scheduledRepository.findById(id1).orElseThrow(() -> new NoSuchElementException(String
                .format("No scheduled worker found with id %d", id1)));
        var scheduledWorker2 = scheduledRepository.findById(id2).orElseThrow(() -> new NoSuchElementException(String
                .format("No scheduled worker found with id %d", id2)));
        var aux = scheduledWorker1.getWorker();
        scheduledWorker1.setWorker(scheduledWorker2.getWorker());
        scheduledWorker2.setWorker(aux);
        scheduledRepository.saveAll(Arrays.asList(scheduledWorker1, scheduledWorker2));
    }

    // Metodo para deletar registros da escala de trabalho por id.
    public void deleteScheduledWorker(DeleteScheduledWorkerDTO deleteScheduledWorkerDTO) {
        var ids = deleteScheduledWorkerDTO.ids();
        var existingIds = scheduledRepository.findExistingIds(ids);
        if (!existingIds.containsAll(ids)) {
            var notFoundIds = ids.stream().filter(id -> !existingIds.contains(id)).toList();
            throw new NoSuchElementException("No scheduled workers found with ids: " + notFoundIds);
        }
        scheduledRepository.deleteAllByIdInBatch(ids);
    }
}
