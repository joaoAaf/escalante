package apisemaperreio.escalante.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SaveScheduledWorkerDTO(
        @NotBlank
        String workDate,
        @NotBlank
        String workerRoleName,
        @NotBlank
        String workerRegistration) {
}
