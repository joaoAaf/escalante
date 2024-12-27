package apisemaperreio.escalante.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WorkerAbsence {

    private Worker worker;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String description;

}
