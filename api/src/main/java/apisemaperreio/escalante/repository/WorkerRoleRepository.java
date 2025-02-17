package apisemaperreio.escalante.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.WorkerRole;

@Repository
public interface WorkerRoleRepository extends JpaRepository<WorkerRole, Integer> {

    List<WorkerRole> findAllByOrderByPriorityAsc();

    Optional<WorkerRole> findByName(String workerRoleName);

}
