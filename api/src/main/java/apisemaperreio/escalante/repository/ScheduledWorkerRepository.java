package apisemaperreio.escalante.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.ScheduledWorker;

@Repository
public interface ScheduledWorkerRepository extends JpaRepository<ScheduledWorker, Integer> {

    Optional<ScheduledWorker> findFirstByWorkerIdOrderByDateDesc(Integer workerId);

}
