package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService {

    private final WorkerRepository workerRepo;

    public List<Worker> getDriver() {
        return workerRepo.findAvailableDriver(LocalDate.parse("2024-09-09"));
    }

}
