package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.WorkerPosition;
import apisemaperreio.escalante.repository.WorkerPositionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerPositionService {

    private final WorkerPositionRepository repo;

    public List<WorkerPosition> getAll() {
        return repo.findAll();
    }

}
