package apisemaperreio.escalante.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.WorkerRole;

@Repository
public interface WorkerRoleRepository extends JpaRepository<WorkerRole, Integer> {

    Optional<WorkerRole> findByName(String string);

}
