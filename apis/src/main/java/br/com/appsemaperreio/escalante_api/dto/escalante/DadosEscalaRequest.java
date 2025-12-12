package br.com.appsemaperreio.escalante_api.dto.escalante;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DadosEscalaRequest(
        @NotNull(message = "A data de início não pode ser nula.")
        LocalDate dataInicio,
        @NotNull(message = "A data de fim não pode ser nula.")
        LocalDate dataFim,
        @NotNull(message = "Dias de serviço não pode ser nulo.")
        @Positive(message = "Dias de serviço deve ser um valor positivo.")
        int diasServico,
        @NotNull(message = "A lista de militares não pode ser nula.")
        @NotEmpty(message = "A lista de militares não pode estar vazia.")
        @Size(max = 100, message = "A lista de militares não pode conter mais de 100 militares.")
        List<@Valid MilitarEscalavel> militares,
        @Size(max = 210, message = "A lista de serviços anteriores não pode conter mais de 210 serviços.")
        List<@Valid ServicoOperacionalDto> servicosAnteriores
    ) {

        public void validarDatas() {
            if (dataFim.isBefore(dataInicio))
                throw new IllegalArgumentException("A data de fim deve ser posterior à data de início.");
            if (dataFim.minusDays(dataInicio.toEpochDay()).toEpochDay() > 35)
                throw new IllegalArgumentException("O intervalo entre a data de início e a data de fim não pode exceder 35 dias.");
        }    

}