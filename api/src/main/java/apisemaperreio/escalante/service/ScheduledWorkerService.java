package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apisemaperreio.escalante.dto.ScheduledWorkerDTO;
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
        if (workerScheduleType.isEmpty() && positionScheduleType.isEmpty()) {
            return Optional.empty();
        }
        if (workerScheduleType.isPresent() && positionScheduleType.isPresent()) {
            return workerScheduleType.get().getDaysOff() >= positionScheduleType.get().getDaysOff() ? workerScheduleType
                    : positionScheduleType;
        }
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
            if (worker.getScheduledWorkers().isEmpty()) {
                return Optional.of(worker);
            }
            var scheduleType = checkScheduleType(worker);
            var lastWork = lastWork(worker);
            if (scheduleType.isPresent()
                    && (scheduleType.get().getDaysOff() > lastWork.getRole().getScheduleType().getDaysOff())) {
                if (lastWork.getDate().plusDays(scheduleType.get().getDaysOff() + 1).isAfter(date)) {
                    continue;
                }
                return Optional.of(worker);
            }
            if (lastWork.getDate().plusDays(lastWork.getRole().getScheduleType().getDaysOff() + 1)
                    .isAfter(date)) {
                continue;
            }
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
        LocalDate newDate = date.plusDays(0);
        for (int i = 1; i <= daysWork; i++) {
            var scheduledWorker = ScheduledWorker.builder()
                    .date(newDate)
                    .worker(worker.orElseThrow())
                    .role(role)
                    .build();
            scheduledWorkersDay.add(scheduledWorker);
            if (i > 1 && i % 2 == 0) {
                if (role.getPriority() == 2) {
                    scheduledWorkersDay.getLast().setRole(roles.get(2));
                } else if (role.getPriority() == 3) {
                    scheduledWorkersDay.getLast().setRole(roles.get(1));
                }

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
    private Optional<Worker> driverEqualsFiscal(LocalDate date, List<ScheduledWorker> scheduledWorkers) {
        var scheduledDriver = scheduledWorkers
                .stream()
                .filter(sw -> sw.getRole().getPriority() == 1 && sw.getDate().equals(date))
                .findFirst();
        var scheduledChefeLinha = scheduledWorkers
                .stream()
                .filter(sw -> sw.getRole().getPriority() == 4 && sw.getDate().equals(date))
                .findFirst();
        if (scheduledDriver.isEmpty()) {
            return Optional.empty();
        }
        if (scheduledChefeLinha.isEmpty()) {
            return Optional.ofNullable(scheduledDriver.get().getWorker());
        }
        return scheduledDriver.get().getWorker().getSeniority() < scheduledChefeLinha.get().getWorker().getSeniority()
                ? Optional.ofNullable(scheduledDriver.get().getWorker())
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
            if (worker.isEmpty()) {
                continue;
            }
            return worker;
        }
        for (var priority : priorities) {
            var worker = selectWorker(date,
                    workerRepository.findAvailableWorkers(date, priority.getPosition().getId(), true));
            if (worker.isEmpty()) {
                continue;
            }
            return worker;
        }
        return Optional.empty();
    }

    // Metodo para salvar os trabalhadores escalados em uma lista, e no banco de
    // dados. Caso não haja trabalhador o metodo não fará nada.
    private void saveScheduledWorkers(Optional<Worker> worker, LocalDate date, List<WorkerRole> roles,
            WorkerRole role, Integer daysWork, List<ScheduledWorker> scheduledWorkers) {
        if (worker.isPresent()) {
            var scheduledWorkersDay = scheduledWorkersDay(worker, date, roles, role, daysWork);
            scheduledWorkers.addAll(scheduledWorkersDay);
            scheduledRepository.saveAll(scheduledWorkersDay);
        }
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
        var scheduledWorkers = new ArrayList<ScheduledWorker>();
        var roles = roleRepository.findAllByOrderByPriorityAsc();
        while (startDate.compareTo(endDate) <= 0) {
            for (var role : roles) {
                Optional<Worker> worker = Optional.empty();
                if (role.getPriority() == 1) {
                    worker = selectWorker(startDate, workerRepository.findAvailableDrivers(startDate));
                    saveScheduledWorkers(worker, startDate, roles, role, daysWork, scheduledWorkers);
                    continue;
                }
                if (role.getPriority() == 5) {
                    var driverEqualsFiscal = driverEqualsFiscal(startDate, scheduledWorkers);
                    if (driverEqualsFiscal.isPresent()) {
                        var newChefeLinha = selectNoDriver(startDate, roles.get(3));
                        if (newChefeLinha.isPresent()
                                && driverEqualsFiscal.get().getSeniority() < newChefeLinha.get().getSeniority()) {
                            worker = driverEqualsFiscal;
                            saveScheduledWorkers(newChefeLinha, startDate, roles, roles.get(3), daysWork,
                                    scheduledWorkers);
                            saveScheduledWorkers(worker, startDate, roles, role, daysWork, scheduledWorkers);
                            break;
                        }
                        worker = selectNoDriver(startDate, role);
                        if (worker.isEmpty()) {
                            worker = driverEqualsFiscal;
                        }
                        saveScheduledWorkers(worker, startDate, roles, role, daysWork, scheduledWorkers);
                        break;
                    }
                }
                worker = selectNoDriver(startDate, role);
                saveScheduledWorkers(worker, startDate, roles, role, daysWork, scheduledWorkers);
            }
            startDate = startDate.plusDays(daysWork);
        }
        scheduledWorkers.sort(Comparator.comparing(ScheduledWorker::getDate).reversed());
        return scheduledWorkers.stream().map(this::toDto).toList();
    }

    public List<ScheduledWorkerDTO> getAllScheduledWorkers() {
        var scheduledWorkers = scheduledRepository.findAll();
        scheduledWorkers.sort(Comparator.comparing(ScheduledWorker::getDate).reversed());
        return scheduledWorkers.stream().map(this::toDto).toList();
    }

}
