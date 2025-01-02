package apisemaperreio.escalante.dto;

import java.time.LocalDate;

public record WorkerDTO(String registration, String name, Character sex, Short seniority, String phone, String email, 
LocalDate birthdate, Boolean driver, Boolean scheduleable, String position) {

}
