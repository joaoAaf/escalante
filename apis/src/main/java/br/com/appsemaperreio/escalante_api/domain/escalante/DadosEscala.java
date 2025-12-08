package br.com.appsemaperreio.escalante_api.domain.escalante;

import java.time.LocalDate;

public record DadosEscala(LocalDate dataInicio, LocalDate dataFim, int diasServico) {

}
