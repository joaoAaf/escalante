package apisemaperreio.escalante.dto;

import lombok.Builder;

@Builder
public record SaveScheduledWorkerDTO( 
                String workDate,
                String workerRoleName,
                String workerRegistration) {
}
