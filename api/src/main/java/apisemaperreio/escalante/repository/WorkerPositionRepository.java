package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.WorkerPosition;

@Repository
public interface WorkerPositionRepository extends JpaRepository<WorkerPosition, Integer> {

}
