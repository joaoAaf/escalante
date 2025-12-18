package br.com.appsemaperreio.escalante_api.escalante.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record MilitarEscalavel(
        @NotNull(message = "A antiguidade do militar não pode ser nula.")
        @Positive(message = "A antiguidade do militar deve ser um valor positivo.")
        Integer antiguidade,
        @NotBlank(message = "A matrícula do militar não pode estar em branco.")
        String matricula,
        @NotBlank(message = "A patente do militar não pode estar em branco.")
        String patente,
        @NotBlank(message = "O nome de paz do militar não pode estar em branco.")
        String nomePaz,
        @NotNull(message = "A data de nascimento do militar não pode ser nula.")
        LocalDate nascimento,
        @NotNull(message = "A folga especial do militar não pode ser nula.")
        @PositiveOrZero(message = "A folga especial do militar deve ser um valor positivo ou zero.")
        @Max(value = 30, message = "A folga especial do militar não pode ser maior que 30.")
        int folgaEspecial,
        @NotNull(message = "C.O.V. não pode ser nulo.")
        Boolean cov) {

}
