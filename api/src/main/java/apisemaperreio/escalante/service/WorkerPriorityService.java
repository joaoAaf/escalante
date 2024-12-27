package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.WorkerPriority;
import apisemaperreio.escalante.repository.WorkerPriorityRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerPriorityService {

    private final WorkerPriorityRepository repo;

    public List<WorkerPriority> getAll() {
        return repo.findAll();
    }

}
