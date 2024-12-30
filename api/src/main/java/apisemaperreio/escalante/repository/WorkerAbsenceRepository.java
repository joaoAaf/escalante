package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.WorkerAbsence;

@Repository
public interface WorkerAbsenceRepository extends JpaRepository<WorkerAbsence, Integer> {

}
