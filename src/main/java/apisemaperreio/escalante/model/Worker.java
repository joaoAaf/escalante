package apisemaperreio.escalante.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Worker {

    private String name;
    private String registrationNumber;
    private Position position;
    private String tel;
    private LocalDate birthdate;
    private LocalDate lastDayWorked;
    private Boolean driver;
    private ScheduleType scheduleType;
    private Boolean scheduleable;

}
