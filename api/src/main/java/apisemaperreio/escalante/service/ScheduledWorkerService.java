package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.repository.ScheduledWorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService {

    private final ScheduledWorkerRepository repo;

    public List<ScheduledWorker> getAll() {
        return repo.findAll();
    }

}
