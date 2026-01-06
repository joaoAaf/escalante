package br.com.appsemaperreio.escalante_api.escalante.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

public record MilitarDto(
        @NotNull(message = "A antiguidade do militar não pode ser nula.")
        @PositiveOrZero(message = "A antiguidade do militar deve ser um valor positivo.")
        @Max(value = 999, message = "A antiguidade do militar não pode ser maior que 999.")
        Integer antiguidade,
        @NotBlank(message = "A matrícula do militar não pode estar em branco.")
        @Size(min = 8, max = 8, message = "A matrícula do militar deve ter exatamente 8 caracteres.")
        String matricula,
        @NotBlank(message = "A patente do militar não pode estar em branco.")
        String patente,
        @NotBlank(message = "O nome de paz do militar não pode estar em branco.")
        @Size(min = 3, max = 40, message = "O nome de paz do militar deve ter entre 3 e 40 caracteres.")
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
