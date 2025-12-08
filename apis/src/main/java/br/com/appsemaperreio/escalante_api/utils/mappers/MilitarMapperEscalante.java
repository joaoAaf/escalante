package br.com.appsemaperreio.escalante_api.utils.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.appsemaperreio.escalante_api.domain.escalante.Militar;
import br.com.appsemaperreio.escalante_api.dtos.escalante.MilitarEscalavel;

@Mapper(componentModel = "spring")
public interface MilitarMapperEscalante {

    @Mapping(target = "ultimosServicos", ignore = true)
    Militar toMilitar(MilitarEscalavel militarEscalavel);

    List<Militar> toListMilitar(List<MilitarEscalavel> militaresEscalaveis);

}
