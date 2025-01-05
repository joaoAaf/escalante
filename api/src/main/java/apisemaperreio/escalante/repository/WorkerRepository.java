package apisemaperreio.escalante.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer> {

    @Query("SELECT w FROM Worker w " +
    "WHERE w.driver = true AND w.scheduleable = true " +
    "AND NOT (MONTH(w.birthdate) = MONTH(:date) AND DAY(w.birthdate) = DAY(:date)) " +
    "AND w.id NOT IN (SELECT wa.worker.id FROM WorkerAbsence wa WHERE wa.startDate <= :date AND wa.endDate >= :date) " +
    "ORDER BY (SELECT sw.date FROM ScheduledWorker sw WHERE sw.worker = w ORDER BY sw.date DESC LIMIT 1) DESC, w.seniority ASC")
    List<Worker> findAvailableDriver(@Param("date") LocalDate date);

    // // Consulta que retorna todos os trabalhadores que são motoristas ordenados
    // // pela data do último dia trabalhado e senioridade em ordem crescente
    // List<Worker> findByDriverOrderByLastDayWorkedAscSeniorityAsc(Boolean driver);
    
    // // Consulta que retorna todos os trabalhadores ordenados pela data do último dia trabalhado em ordem crescente
    // // e pela senioridade em ordem decrescente
    // @Query("SELECT w FROM Worker w JOIN w.position p WHERE p.name = :positionName ORDER BY w.lastDayWorked ASC, w.seniority DESC")
    // List<Worker> findByPositionNameSeniorityDesc(@Param("positionName") String positionName);

    // // Consulta que retorna todos os trabalhadores ordenados pela data do último dia trabalhado em ordem crescente
    // // e pela senioridade em ordem crescente
    // @Query("SELECT w FROM Worker w JOIN w.position p WHERE p.name = :positionName ORDER BY w.lastDayWorked ASC, w.seniority ASC")
    // List<Worker> findByPositionNameSeniorityAsc(@Param("positionName") String positionName);

}
