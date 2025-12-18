package br.com.appsemaperreio.escalante_api.escalante.model.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarEscalavel;

@Mapper(componentModel = "spring")
public interface MilitarMapper {

    @Mapping(target = "ultimosServicos", ignore = true)
    Militar toMilitar(MilitarEscalavel militarEscalavel);

    List<Militar> toListMilitar(List<MilitarEscalavel> militaresEscalaveis);

}
