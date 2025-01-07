package apisemaperreio.escalante.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.WorkerPriority;
import apisemaperreio.escalante.model.WorkerRole;


@Repository
public interface WorkerPriorityRepository extends JpaRepository<WorkerPriority, Integer> {

    List<WorkerPriority> findByRoleOrderByPriorityAsc(WorkerRole role);

}
