package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.WorkerAbsence;
import apisemaperreio.escalante.repository.WorkerAbsenceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerAbsenceService {

    private final WorkerAbsenceRepository repo;

    public List<WorkerAbsence> getAll() {
        return repo.findAll();
    }

}
