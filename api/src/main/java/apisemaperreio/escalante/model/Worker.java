package apisemaperreio.escalante.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Worker {

    private String registration;
    private String name;
    private Character sex;
    private Short rank;
    private String phone;
    private String email;
    private LocalDate birthdate;
    private LocalDate lastDayWorked;
    private Boolean driver;
    private Boolean scheduleable;
    private WorkerPosition position;
    private ScheduleType scheduleType;

}
