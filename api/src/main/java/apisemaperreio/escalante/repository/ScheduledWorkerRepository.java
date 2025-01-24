package apisemaperreio.escalante.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.ScheduledWorker;

@Repository
public interface ScheduledWorkerRepository extends JpaRepository<ScheduledWorker, Integer> {

        // Consulta que retorna a escala de trabalho de um certo dia
        @Query("SELECT s FROM ScheduledWorker s " +
                        "LEFT JOIN FETCH s.worker w LEFT JOIN FETCH s.role r LEFT JOIN FETCH r.scheduleType " +
                        "WHERE s.date = :date ORDER BY s.role.priority DESC, w.seniority DESC")
        List<ScheduledWorker> findByDate(@Param("date") LocalDate date);

        // Consulta que retorna um registro da escala de trabalho por id
        @Query("SELECT s FROM ScheduledWorker s " +
                        "LEFT JOIN FETCH s.worker w LEFT JOIN FETCH s.role r LEFT JOIN FETCH r.scheduleType " +
                        "WHERE s.id = :id")
        Optional<ScheduledWorker> findById(@Param("id") Integer id);

        // Consulta que retorna a escala de trabalho de um certo intervalo de tempo
        @Query("SELECT s FROM ScheduledWorker s " +
                        "LEFT JOIN FETCH s.worker w LEFT JOIN FETCH s.role r LEFT JOIN FETCH r.scheduleType " +
                        "WHERE s.date BETWEEN :startDate AND :endDate " +
                        "ORDER BY s.date DESC, s.role.priority DESC, w.seniority DESC")
        List<ScheduledWorker> findByRangeDates(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        // Consulta que retorna os dias que certo trabalhador trabalhou
        // em um certo intervalo de tempo
        @Query("SELECT s FROM ScheduledWorker s " +
                        "LEFT JOIN FETCH s.worker w LEFT JOIN FETCH s.role r LEFT JOIN FETCH r.scheduleType " +
                        "WHERE s.worker.registration = :workerRegistration " +
                        "AND s.date BETWEEN :startDate AND :endDate ORDER BY s.date DESC, s.role.priority DESC")
        List<ScheduledWorker> findScheduledWorkerRangeDate(@Param("workerRegistration") String workerRegistration,
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        // Consulta que retorna os dias que possuem menos de 5 trabalhadores escalados
        // mesmo que haja um trabalhador exercendo mais de uma função
        @Query("SELECT s FROM ScheduledWorker s " +
                        "LEFT JOIN FETCH s.worker w LEFT JOIN FETCH s.role r LEFT JOIN FETCH r.scheduleType " +
                        "WHERE s.date IN (SELECT sw.date FROM ScheduledWorker sw " +
                        "GROUP BY sw.date HAVING COUNT(sw) < 5 OR " +
                        "(COUNT(sw) = 5 AND COUNT(DISTINCT sw.worker) < 5)) " +
                        "AND (s.date BETWEEN :startDate AND :endDate) " +
                        "ORDER BY s.date DESC, s.role.priority DESC, w.seniority DESC")
        List<ScheduledWorker> findInconsistencies(@Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);

        // Consulta para verificar se existem trabalhadores escalados em um determinado
        // intervalo de tempo
        @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM ScheduledWorker s " +
                        "WHERE s.date BETWEEN :startDate AND :endDate")
        Boolean existsInRangeDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        // Consulta para verificar se os ids dos trabalhadores escalados não existem
        @Query("SELECT s.id FROM ScheduledWorker s WHERE s.id IN :ids")
        List<Integer> findExistingIds(@Param("ids") List<Integer> ids);

}
