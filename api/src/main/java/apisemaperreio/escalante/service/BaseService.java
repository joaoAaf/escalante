package apisemaperreio.escalante.service;

import apisemaperreio.escalante.dto.ScheduledWorkerDTO;
import apisemaperreio.escalante.dto.SimpleWorkerDTO;
import apisemaperreio.escalante.dto.WorkerDTO;
import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.model.Worker;

public class BaseService {

    protected WorkerDTO toDto(Worker worker) {
        return WorkerDTO.builder()
                .registration(worker.getRegistration())
                .name(worker.getName())
                .sex(worker.getSex())
                .seniority(worker.getSeniority())
                .position(worker.getPosition().getName())
                .phone(worker.getPhone())
                .email(worker.getEmail())
                .birthdate(worker.getBirthdate())
                .driver(worker.getDriver())
                .scheduleable(worker.getScheduleable())
                .build();
    }

    protected SimpleWorkerDTO toSimpleDto(Worker worker) {
        return SimpleWorkerDTO.builder()
                .registration(worker.getRegistration())
                .name(worker.getName())
                .seniority(worker.getSeniority())
                .position(worker.getPosition().getName())
                .build();
    }

    protected ScheduledWorkerDTO toDto(ScheduledWorker scheduledWorker) {
        return ScheduledWorkerDTO.builder()
                .workDate(scheduledWorker.getDate())
                .workerRole(scheduledWorker.getRole().getName())
                .scheduleType(scheduledWorker.getRole().getScheduleType().getName())
                .worker(toSimpleDto(scheduledWorker.getWorker()))
                .build();
    }

}
