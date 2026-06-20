package br.com.appsemaperreio.escalante_api.escalante.model.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServicoAnteriorDto(
        @NotNull(message = "A data do serviço não pode ser nula.")
        LocalDate dataServico,
        @NotBlank(message = "A matrícula do militar não pode estar em branco.")
        String matricula,
        @NotBlank(message = "A função do serviço não pode estar em branco.")
        String funcao) {

}