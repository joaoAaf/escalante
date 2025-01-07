package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService extends BaseService {

    private final WorkerRepository workerRepo;
    private final ScheduledWorkerRepository scheduledRepo;
    private final WorkerRoleRepository roleRepo;
    private final WorkerPriorityRepository priorityRepo;

    private Optional<ScheduleType> checkScheduleType(Worker worker) {
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

    private ScheduledWorker lastWork(Worker worker) {
        var workedDays = new ArrayList<>(worker.getScheduledWorkers());
        workedDays.sort(Comparator.comparing(ScheduledWorker::getDate).reversed());
        return workedDays.getFirst();
    }

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

    public List<ScheduledWorker> scheduledWorkersDay(Optional<Worker> worker, LocalDate date, List<WorkerRole> roles,
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

    private Optional<Worker> selectNoDriver(LocalDate date, Integer positionId) {
        var worker = selectWorker(date, workerRepo.findAvailableWorkers(date, positionId, false));
        if (worker.isEmpty()) {
            worker = selectWorker(date, workerRepo.findAvailableWorkers(date, positionId, true));
        }
        return worker;
    }

    public List<ScheduledWorkerDTO> scheduler(LocalDate startDate, LocalDate endDate, Integer daysWork) {
        var scheduledWorkers = new ArrayList<ScheduledWorker>();
        var roles = roleRepo.findAll();
        roles.sort(Comparator.comparing(WorkerRole::getPriority));
        while (startDate.compareTo(endDate) <= 0) {
            for (var role : roles) {
                if (role.getPriority() == 1) {
                    var worker = selectWorker(startDate, workerRepo.findAvailableDrivers(startDate));
                    var scheduledWorkersDay = scheduledWorkersDay(worker, startDate, roles, role, daysWork);
                    scheduledWorkers.addAll(scheduledWorkersDay);
                    scheduledRepo.saveAll(scheduledWorkersDay);
                    continue;
                }
                Optional<Worker> worker = Optional.empty();
                var priorities = priorityRepo.findByRoleOrderByPriorityAsc(role);
                for (var priority : priorities) {
                    if (role.getPriority() == 5) {
                        LocalDate finalDate = startDate.plusDays(0);
                        var scheduledDriver = scheduledWorkers
                                .stream()
                                .filter(sw -> sw.getRole().getPriority() == 1 && sw.getDate().equals(finalDate))
                                .findFirst();
                        var scheduledLinha = scheduledWorkers
                                .stream()
                                .filter(sw -> sw.getRole().getPriority() == 4 && sw.getDate().equals(finalDate))
                                .findFirst();
                        if (scheduledDriver.get().getWorker().getSeniority() < scheduledLinha.get().getWorker()
                                .getSeniority()) {
                            worker = Optional.ofNullable(scheduledDriver.get().getWorker());
                            var priorityLinha = priorities.get(3);
                            var workerLinha = selectNoDriver(startDate, priorityLinha.getPosition().getId());
                            var scheduledWorkersDay = scheduledWorkersDay(workerLinha, startDate, roles, roles.get(3), daysWork);
                            scheduledWorkers.addAll(scheduledWorkersDay);
                            scheduledRepo.saveAll(scheduledWorkersDay);
                            break;
                        }
                    }
                    worker = selectNoDriver(startDate, priority.getPosition().getId());
                    if (worker.isPresent()) break;
                }
                var scheduledWorkersDay = scheduledWorkersDay(worker, startDate, roles, role, daysWork);
                scheduledWorkers.addAll(scheduledWorkersDay);
                scheduledRepo.saveAll(scheduledWorkersDay);
            }
            startDate = startDate.plusDays(daysWork);
        }
        return scheduledWorkers.stream().map(this::toDto).toList();
    }

    public List<ScheduledWorkerDTO> getAllScheduledWorkers() {
        var scheduledWorkers = scheduledRepo.findAll();
        scheduledWorkers.sort(Comparator.comparing(ScheduledWorker::getDate).reversed());
        return scheduledWorkers.stream().map(this::toDto).toList();
    }

}
