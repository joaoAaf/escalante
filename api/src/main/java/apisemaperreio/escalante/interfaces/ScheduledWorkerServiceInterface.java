package apisemaperreio.escalante.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import apisemaperreio.escalante.dto.CountScheduledWorkerDTO;
import apisemaperreio.escalante.dto.DeleteScheduledWorkerDTO;
import apisemaperreio.escalante.dto.SaveScheduledWorkerDTO;
import apisemaperreio.escalante.dto.ScheduledWorkerDTO;
import apisemaperreio.escalante.dto.UpdateScheduledWorkerDTO;

public interface ScheduledWorkerServiceInterface {

    public List<ScheduledWorkerDTO> scheduler(LocalDate startDate, LocalDate endDate, Integer daysWork);

    public Optional<ScheduledWorkerDTO> getScheduledWorkerById(Integer id);

    public List<ScheduledWorkerDTO> getScheduledWorkerByDate(LocalDate date);

    public List<ScheduledWorkerDTO> getAllScheduledWorkersRangeDate(LocalDate startDate, LocalDate endDate);

    public List<ScheduledWorkerDTO> getScheduledWorkerRangeDate(String workerRegistration, LocalDate startDate,
            LocalDate endDate);

    public List<CountScheduledWorkerDTO> getAllWorkersCountDays(LocalDate startDate, LocalDate endDate);

    public Optional<CountScheduledWorkerDTO> getWorkerCountDays(String workerRegistration, LocalDate startDate,
            LocalDate endDate);

    public List<ScheduledWorkerDTO> getInconsistencies(LocalDate startDate, LocalDate endDate);

    public ScheduledWorkerDTO saveScheduledWorker(SaveScheduledWorkerDTO saveScheduledWorkerDto);

    public void updateScheduledWorker(Integer id, UpdateScheduledWorkerDTO updateScheduledWorkerDto);

    public void swapTwoScheduledWorkers(Integer id1, Integer id2);

    public void deleteScheduledWorker(DeleteScheduledWorkerDTO deleteScheduledWorkerDTO);

    public void deleteScheduledWorkersRangeDate(LocalDate startDate, LocalDate endDate);

    public void deleteScheduledWorkersByDate(LocalDate date);

}
