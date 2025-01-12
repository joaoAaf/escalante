package apisemaperreio.escalante.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.ScheduledWorker;

@Repository
public interface ScheduledWorkerRepository extends JpaRepository<ScheduledWorker, Integer> {

    List<ScheduledWorker> findByDate(LocalDate date);

    @Query("SELECT s FROM ScheduledWorker s WHERE s.date BETWEEN :startDate AND :endDate " +
            "ORDER BY s.date DESC")
    List<ScheduledWorker> findByRangeDatesAsc(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
