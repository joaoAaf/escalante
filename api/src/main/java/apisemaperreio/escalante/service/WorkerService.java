package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.dto.WorkerDTO;
import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerService extends BaseService {

    private final WorkerRepository repo;

    public List<WorkerDTO> getAll() {
        var workers = repo.findAll();
        return workers.stream().map(this::toDto).toList();
    }

    public List<Worker> getAvailableDrivers(LocalDate date) {
        return repo.findAvailableDrivers(date);
    }

    public List<Worker> getAvailableWorkers(LocalDate date, Integer positionId, Boolean driver) {
        return repo.findAvailableWorkers(date, positionId, driver);
    }

}
