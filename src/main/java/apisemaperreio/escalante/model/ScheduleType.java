package apisemaperreio.escalante.model;

import lombok.Data;

@Data
public class ScheduleType {

    private String name;
    private Integer daysWorked;
    private Integer daysOff;
    private Integer multiplier;

}
