package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import apisemaperreio.escalante.model.WorkerPosition;

public interface PositionRepository extends JpaRepository<WorkerPosition, Integer> {

}
