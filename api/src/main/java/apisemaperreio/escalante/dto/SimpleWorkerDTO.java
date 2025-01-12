package apisemaperreio.escalante.dto;

import lombok.Builder;

@Builder
public record SimpleWorkerDTO(
                String registration,
                String name,
                Short seniority,
                String position,
                Boolean driver) {
}
