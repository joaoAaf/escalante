package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.WorkerPosition;
import apisemaperreio.escalante.repository.PositionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerPositionService {

    private final PositionRepository repo;

    public List<WorkerPosition> getAll() {
        return repo.findAll();
    }

}
