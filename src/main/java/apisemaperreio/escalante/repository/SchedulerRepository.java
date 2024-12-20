package apisemaperreio.escalante.repository;

import java.util.List;

import apisemaperreio.escalante.model.Absence;
import apisemaperreio.escalante.model.Position;
import apisemaperreio.escalante.model.Role;
import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.model.Worker;
import lombok.Data;

@Data
public class SchedulerRepository {

    private List<Worker> workers;
    private List<Position> positions;
    private List<Role> roles;
    private List<Absence> absences;
    private List<ScheduleType> ScheduleTypes;
    private List<ScheduledWorker> scheduledWorkers;

}
