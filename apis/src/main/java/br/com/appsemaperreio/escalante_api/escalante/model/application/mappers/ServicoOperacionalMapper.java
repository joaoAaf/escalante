package br.com.appsemaperreio.escalante_api.escalante.model.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.ServicoOperacional;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;

@Mapper(componentModel = "spring")
public interface ServicoOperacionalMapper {

    @Mapping(target = "matricula", source = "militar.matricula")
    @Mapping(target = "nomePaz", source = "militar.nomePaz")
    @Mapping(target = "patente", source = "militar.patente.nome")
    @Mapping(target = "antiguidade", source = "militar.antiguidade")
    @Mapping(target = "funcao", source = "funcao.nome")
    ServicoOperacionalDto toServicoOperacionalDto(ServicoOperacional servicoOperacional);

    List<ServicoOperacionalDto> toListServicoOperacionalDto(List<ServicoOperacional> servicosEscala);

}
