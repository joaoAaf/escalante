package apisemaperreio.escalante.escalante.utils.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import apisemaperreio.escalante.escalante.domain.escalante.ServicoOperacional;
import apisemaperreio.escalante.escalante.dtos.ServicoOperacionalDto;

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
