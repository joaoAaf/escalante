package apisemaperreio.escalante.service;

import java.util.Optional;

import apisemaperreio.escalante.dto.ScheduledWorkerDTO;
import apisemaperreio.escalante.dto.SimpleScheduleTypeDTO;
import apisemaperreio.escalante.dto.SimpleWorkerDTO;
import apisemaperreio.escalante.dto.WorkerDTO;
import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.model.Worker;

public class BaseService {

    protected WorkerDTO toDto(Worker worker) {
        return WorkerDTO.builder()
                .id(worker.getId())
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
                .scheduleType(toSimpleDto(worker.getScheduleType()))
                .build();
    }

    protected ScheduledWorkerDTO toDto(ScheduledWorker scheduledWorker) {
        return ScheduledWorkerDTO.builder()
                .id(scheduledWorker.getId())
                .workDate(scheduledWorker.getDate())
                .workerRole(scheduledWorker.getRole().getName())
                .scheduleType(toSimpleDto(scheduledWorker.getRole().getScheduleType()))
                .worker(toSimpleDto(scheduledWorker.getWorker()))
                .build();
    }

    protected SimpleWorkerDTO toSimpleDto(Worker worker) {
        return SimpleWorkerDTO.builder()
                .registration(worker.getRegistration())
                .name(worker.getName())
                .seniority(worker.getSeniority())
                .position(worker.getPosition().getName())
                .driver(worker.getDriver())
                .build();
    }

    protected SimpleScheduleTypeDTO toSimpleDto(ScheduleType scheduleType) {
        var scheduleTypeOpt = Optional.ofNullable(scheduleType);
        return scheduleTypeOpt.isPresent() ? SimpleScheduleTypeDTO.builder()
                .name(scheduleType.getName())
                .daysOff(scheduleType.getDaysOff())
                .build() : null;
    }

    protected boolean validadeUpdate(String attOld, String attNew) {
        return Optional.ofNullable(attNew).isPresent() && !attNew.isBlank() && !attOld.equals(attNew);
    }

}
