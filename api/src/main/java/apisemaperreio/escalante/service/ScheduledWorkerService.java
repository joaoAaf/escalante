package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService extends BaseService {

    private final WorkerRepository workerRepo;
    
    public List<Worker> getDrivers(LocalDate date) {
        return workerRepo.findAvailableDriver(date);
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

}
