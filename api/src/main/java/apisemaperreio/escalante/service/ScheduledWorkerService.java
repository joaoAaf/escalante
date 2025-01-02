package apisemaperreio.escalante.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.dto.WorkerDTO;
import apisemaperreio.escalante.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduledWorkerService {

    private final WorkerRepository workerRepo;

    public List<WorkerDTO> getDriver() {
        var workers = workerRepo.findAvailableDriver(LocalDate.parse("2024-09-09"));
        var workersDTO = new ArrayList<WorkerDTO>();
        for (var worker : workers) {
            var workerDTO = new WorkerDTO(worker.getRegistration(), worker.getName(), worker.getSex(), worker.getSeniority(),
            worker.getPhone(), worker.getEmail(), worker.getBirthdate(), worker.getDriver(), worker.getScheduleable(),
            worker.getPosition().getName());
            workersDTO.add(workerDTO);
        }
        return workersDTO;
    }

}
