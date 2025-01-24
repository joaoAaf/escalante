package apisemaperreio.escalante.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteScheduledWorkerDTO(
        List<@NotNull(message = "IDs must not be null or empty") @Positive(message = "IDs must be positive") Integer> ids) {

}
