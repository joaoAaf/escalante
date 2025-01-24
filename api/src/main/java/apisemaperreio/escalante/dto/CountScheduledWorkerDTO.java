package apisemaperreio.escalante.dto;

import lombok.Builder;

@Builder
public record CountScheduledWorkerDTO(SimpleWorkerDTO worker, Long count) {

}
