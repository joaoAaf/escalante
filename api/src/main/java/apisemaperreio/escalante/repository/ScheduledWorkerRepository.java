package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.ScheduledWorker;

@Repository
public interface ScheduledWorkerRepository extends JpaRepository<ScheduledWorker, Integer> {

}
