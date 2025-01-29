package apisemaperreio.escalante.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {

        Optional<Worker> findByRegistration(String registration);

        // Consulta que retorna todos os trabalhadores disponiveis que são motoristas
        // ordenados pela data do último dia trabalhado e senioridade em ordem crescente
        @Query("SELECT w FROM Worker w WHERE w.driver = true AND w.scheduleable = true " +
                        "AND NOT (MONTH(w.birthdate) = MONTH(:date) AND DAY(w.birthdate) = DAY(:date)) " +
                        "AND w.id NOT IN (SELECT wa.worker.id FROM WorkerAbsence wa " +
                        "WHERE wa.startDate <= :date AND wa.endDate >= :date) " +
                        "ORDER BY (SELECT MAX(sw.date) FROM ScheduledWorker sw WHERE sw.worker = w) ASC, w.seniority ASC")
        List<Worker> findAvailableDrivers(@Param("date") LocalDate date);

        // Consulta que retorna todos os trabalhadores disponiveis ordenados pela data
        // do último dia trabalhado em ordem crescente
        // e senioridade em ordem decrescente
        @Query("SELECT w FROM Worker w " +
                        "WHERE w.driver = :driver AND w.scheduleable = true AND w.position.id = :positionId " +
                        "AND NOT (MONTH(w.birthdate) = MONTH(:date) AND DAY(w.birthdate) = DAY(:date)) " +
                        "AND w.id NOT IN (SELECT wa.worker.id FROM WorkerAbsence wa " +
                        "WHERE wa.startDate <= :date AND wa.endDate >= :date) " +
                        "ORDER BY (SELECT MAX(sw.date) FROM ScheduledWorker sw WHERE sw.worker = w) ASC, w.seniority DESC")
        List<Worker> findAvailableWorkers(@Param("date") LocalDate date, @Param("positionId") Integer positionId,
                        @Param("driver") Boolean driver);

        // Consulta que retorna quantos dias um trabalhador trabalhou em um certo
        // período de tempo
        @Query("SELECT w, COUNT(DISTINCT sw.date) FROM Worker w " +
                        "LEFT JOIN FETCH w.position p LEFT JOIN w.scheduledWorkers sw " +
                        "WHERE w.registration = :workerRegistration AND (sw.date BETWEEN :startDate AND :endDate) " +
                        "GROUP BY w.id")
        List<Object[]> findByWorkerCountWorkedDays(@Param("workerRegistration") String workerRegistration,
                        @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

        // Consulta que retorna quantos dias cada trabalhador trabalhou em um certo
        // período de tempo
        @Query("SELECT w, COUNT(DISTINCT sw.date) FROM Worker w " +
                        "LEFT JOIN FETCH w.position p LEFT JOIN FETCH w.scheduleType st " +
                        "LEFT JOIN w.scheduledWorkers sw WHERE sw.date BETWEEN :startDate " +
                        "AND :endDate GROUP BY w.id ORDER BY w.seniority ASC")
        List<Object[]> findAllWorkersCountWorkedDays(LocalDate startDate, LocalDate endDate);

        // Consulta retorna um trabalhador que trabalhou em determinado dia e função
        @Query("SELECT w FROM Worker w LEFT JOIN w.scheduledWorkers sw " +
                        "WHERE sw.date = :date AND sw.role.priority = :rolePriority " +
                        "ORDER BY w.seniority DESC LIMIT 1")
        Optional<Worker> findByWorkedDateAndRolePriority(@Param("date") LocalDate date,
                        @Param("rolePriority") Integer rolePriority);

}
