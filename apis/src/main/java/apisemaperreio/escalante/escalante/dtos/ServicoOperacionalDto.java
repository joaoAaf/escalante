package apisemaperreio.escalante.escalante.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

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
        @PositiveOrZero(message = "A folga do militar deve ser um valor positivo ou zero.")
        int folga) {

}
