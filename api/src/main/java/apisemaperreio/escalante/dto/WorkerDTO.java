package apisemaperreio.escalante.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record WorkerDTO(
                Integer id,
                String registration,
                String name,
                Character sex,
                Short seniority,
                String position,
                String phone,
                String email,
                LocalDate birthdate,
                Boolean driver,
                Boolean scheduleable,
                SimpleScheduleTypeDTO scheduleType) {

}
