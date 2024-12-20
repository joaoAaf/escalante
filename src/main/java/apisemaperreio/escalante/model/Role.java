package apisemaperreio.escalante.model;

import lombok.Data;

@Data
public class Role {

    private String name;
    private Integer minimumQuantity;
    private ScheduleType scheduleType;

}
