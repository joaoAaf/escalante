package apisemaperreio.escalante.dto;

import lombok.Builder;

@Builder
public record SimpleScheduleTypeDTO(
    String name,
    Short daysOff) {
}
