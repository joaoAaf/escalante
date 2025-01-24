package apisemaperreio.escalante.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record UpdateScheduledWorkerDTO(
        String workDate,
        String workerRoleName,
        String workerRegistration) {
}
