package br.com.appsemaperreio.escalante_api.escalante.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ServicoOperacionalDto(
        @NotNull(message = "A data do serviço não pode ser nula.")
        LocalDate dataServico,
        @NotBlank(message = "A matrícula do militar não pode estar em branco.")
        String matricula,
        @NotBlank(message = "O nome de paz do militar não pode estar em branco.")
        String nomePaz,
        @NotBlank(message = "A patente do militar não pode estar em branco.")
        String patente,
        @NotNull(message = "A antiguidade do militar não pode ser nula.")
        @Positive(message = "A antiguidade do militar deve ser um valor positivo.")
        Integer antiguidade,
        @NotBlank(message = "A função do militar não pode estar em branco.")
        String funcao,
        @NotNull(message = "A folga do militar não pode ser nula.")
        @Positive(message = "A folga do militar deve ser um valor positivo.")
        @Max(value = 30, message = "A folga do militar não pode ser maior que 30.")
        int folga) {

}
