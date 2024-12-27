package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.WorkerPriority;

@Repository
public interface WorkerPriorityRepository extends JpaRepository<WorkerPriority, Integer> {

}
