package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.repository.ScheduledWorkerRepository;
import apisemaperreio.escalante.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService extends BaseService {

    private final WorkerRepository workerRepo;
    private final ScheduledWorkerRepository scheduledRepo;
    
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
        var notScheduleds = new ArrayList<Worker>();
        for (int i = workers.size() - 1; i >= 0; i--) {
            if (!workers.get(i).getScheduledWorkers().isEmpty()) {
                break;
            }
            notScheduleds.add(workers.get(i));
        }
        if (notScheduleds.isEmpty()) {
            return Optional.empty();
        }
        notScheduleds.sort(Comparator.comparing(Worker::getSeniority));
        return Optional.of(notScheduleds.get(0));
    }
    
    public Optional<Worker> selectWorker(LocalDate date) {
        var workers = workerRepo.findAvailableDrivers(date);
        var notScheduled = notScheduledWorker(workers);
        if (notScheduled.isPresent()) {    
            return notScheduled;
        }   
        for (var worker : workers) {
            var scheduleType = checkScheduleType(worker);
            var dateLastWorked = dateLastWorked(worker);
            if (scheduleType.isPresent() && (scheduleType.get().getDaysOff() > dateLastWorked.getRole().getScheduleType().getDaysOff())) {
                if (dateLastWorked.getDate().plusDays(scheduleType.get().getDaysOff() + 1).isAfter(date)) {
                    continue;
                }
                return Optional.of(worker);
            }
            if (dateLastWorked.getDate().plusDays(dateLastWorked.getRole().getScheduleType().getDaysOff() + 1).isAfter(date)) {
                continue;
            }
            return Optional.of(worker);
        }
        return Optional.empty();
    }
    
    // public WorkerDTO scheduler(LocalDate date, WorkerRole role) {
    //     if (role.getName().equals("motorista")) { 
            
    //     }
    //     return null;
    // }

}
