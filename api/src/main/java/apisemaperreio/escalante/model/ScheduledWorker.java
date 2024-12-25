package apisemaperreio.escalante.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ScheduledWorker {

    private Worker worker;
    private LocalDate dateScheduled;
    private WorkerRole role;

}
