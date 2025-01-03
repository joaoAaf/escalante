package apisemaperreio.escalante.service;

import apisemaperreio.escalante.dto.WorkerDTO;
import apisemaperreio.escalante.model.Worker;

public class BaseService {

    protected WorkerDTO workerToDto(Worker worker) {
        return WorkerDTO.builder()
                .registration(worker.getRegistration())
                .name(worker.getName())
                .sex(worker.getSex())
                .seniority(worker.getSeniority())
                .phone(worker.getPhone())
                .email(worker.getEmail())
                .birthdate(worker.getBirthdate())
                .driver(worker.getDriver())
                .scheduleable(worker.getScheduleable())
                .position(worker.getPosition().getName())
                .build();
    }

}
