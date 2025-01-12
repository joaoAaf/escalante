package apisemaperreio.escalante.dto;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record ScheduledWorkerDTO(
        LocalDate workDate,
        String workerRole,
        SimpleScheduleTypeDTO scheduleType,
        SimpleWorkerDTO worker) {
}
