package br.com.appsemaperreio.escalante_api.escalante.domain;

import java.time.LocalDate;

public record DadosEscala(LocalDate dataInicio, LocalDate dataFim, int diasServico) {

}
