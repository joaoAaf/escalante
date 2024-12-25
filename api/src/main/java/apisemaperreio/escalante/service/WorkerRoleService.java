package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.WorkerRole;
import apisemaperreio.escalante.repository.WorkerRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WorkerRoleService {

    private final WorkerRoleRepository repo;

    public List<WorkerRole> getAll() {
        return repo.findAll();
    }

}
