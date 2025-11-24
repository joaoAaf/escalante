package apisemaperreio.escalante.escalante.dtos;

import java.time.LocalDate;
import java.util.List;

public record DadosEscalaRequest(LocalDate dataInicio, LocalDate dataFim, int diasServico, List<MilitarEscalavel> militares, List<ServicoOperacionalDto> servicosAnteriores) {
}