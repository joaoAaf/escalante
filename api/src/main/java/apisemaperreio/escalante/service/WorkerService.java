package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerService {

    private final WorkerRepository repo;

    public List<Worker> getAll() {
        return repo.findAll();
    }

    public List<Worker> getAvailableDrivers(LocalDate date) {
        return repo.findAvailableDrivers(date);
    }

}
