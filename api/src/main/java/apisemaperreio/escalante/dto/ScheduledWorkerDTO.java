package apisemaperreio.escalante.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record ScheduledWorkerDTO(
                Integer  id, 
                LocalDate workDate,
                String workerRole,
                SimpleScheduleTypeDTO scheduleType,
                SimpleWorkerDTO worker) {
}
