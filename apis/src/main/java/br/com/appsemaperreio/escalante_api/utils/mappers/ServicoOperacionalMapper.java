package br.com.appsemaperreio.escalante_api.utils.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.appsemaperreio.escalante_api.domain.escalante.ServicoOperacional;
import br.com.appsemaperreio.escalante_api.dtos.ServicoOperacionalDto;

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
