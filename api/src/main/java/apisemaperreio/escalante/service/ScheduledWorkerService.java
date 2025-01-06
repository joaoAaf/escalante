package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.model.WorkerRole;
import apisemaperreio.escalante.repository.ScheduledWorkerRepository;
import apisemaperreio.escalante.repository.WorkerRepository;
import apisemaperreio.escalante.repository.WorkerRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService extends BaseService {

    private final WorkerRepository workerRepo;
    private final ScheduledWorkerRepository scheduledRepo;
    // private final WorkerPositionRepository positionRepo;
    private final WorkerRoleRepository roleRepo;

    public List<ScheduledWorker> getAll() {
        return scheduledRepo.findAll();
    }

    public List<Worker> getAvailableWorkers(LocalDate date, Integer positionId, Boolean driver) {
        return workerRepo.findAvailableWorkers(date, positionId, driver);
    }

    public List<Worker> getAvailableDrivers(LocalDate date) {
        return workerRepo.findAvailableDrivers(date);
    }

    public Optional<ScheduleType> checkScheduleType(Worker worker) {
        var workerScheduleType = Optional.ofNullable(worker.getScheduleType());
        var positionScheduleType = Optional.ofNullable(worker.getPosition().getScheduleType());
        if (workerScheduleType.isEmpty() && positionScheduleType.isEmpty()) {
            return Optional.empty();
        }
        if (workerScheduleType.isEmpty()) {
            return positionScheduleType;
        }
        if (workerScheduleType.get().getDaysOff() >= positionScheduleType.get().getDaysOff()) {
            return workerScheduleType;
        }
        return positionScheduleType;
    }

    public ScheduledWorker dateLastWorked(Worker worker) {
        var workedDays = new ArrayList<>(worker.getScheduledWorkers());
        workedDays.sort(Comparator.comparing(ScheduledWorker::getDate).reversed());
        return workedDays.get(0);
    }

    public Optional<Worker> notScheduledWorker(List<Worker> workers) {
        var notScheduleds = new LinkedList<Worker>();
        for (int i = workers.size() - 1; i >= 0; i--) {
            if (!workers.get(i).getScheduledWorkers().isEmpty()) {
                break;
            }
            notScheduleds.addFirst(workers.get(i));
        }
        return Optional.ofNullable(notScheduleds.peekFirst());
    }

    public Optional<Worker> selectWorker(LocalDate date, List<Worker> workers) {
        var notScheduled = notScheduledWorker(workers);
        if (notScheduled.isPresent()) {
            return notScheduled;
        }
        for (var worker : workers) {
            var scheduleType = checkScheduleType(worker);
            var dateLastWorked = dateLastWorked(worker);
            if (scheduleType.isPresent()
                    && (scheduleType.get().getDaysOff() > dateLastWorked.getRole().getScheduleType().getDaysOff())) {
                if (dateLastWorked.getDate().plusDays(scheduleType.get().getDaysOff() + 1).isAfter(date)) {
                    continue;
                }
                return Optional.of(worker);
            }
            if (dateLastWorked.getDate().plusDays(dateLastWorked.getRole().getScheduleType().getDaysOff() + 1)
                    .isAfter(date)) {
                continue;
            }
            return Optional.of(worker);
        }
        return Optional.empty();
    }

    public List<ScheduledWorker> scheduler(LocalDate startDate, LocalDate endDate) {
        var scheduledWorkers = new ArrayList<ScheduledWorker>();
        var roles = roleRepo.findAll();
        roles.sort(Comparator.comparing(WorkerRole::getPriority));
        while (startDate.compareTo(endDate) <= 0) {
            for (var role : roles) {
                switch (role.getPriority()) {
                    case 1:
                        // TODO: Código para escolher o motorista
                        var driver = selectWorker(startDate, workerRepo.findAvailableDrivers(startDate));
                        var scheduledWorker = ScheduledWorker.builder()
                            .date(startDate)
                            .worker(driver.orElseThrow())
                            .role(role)
                            .build();
                        scheduledWorkers.add(scheduledWorker);
                        break;
                    case 2:
                        // TODO: Código para escolher o permanente
                        break;
                    case 3:
                        // TODO: Código para escolher o aux. de linha
                        break;
                    case 4:
                        // TODO: Código para escolher o chefe de linha
                        break;
                    case 5:
                        // TODO: Código para escolher o fiscal
                        break;
                    default:
                        break;
                }
            }
            startDate = startDate.plusDays(1);
        }
        scheduledRepo.saveAll(scheduledWorkers);
        return scheduledWorkers;
    }

}
