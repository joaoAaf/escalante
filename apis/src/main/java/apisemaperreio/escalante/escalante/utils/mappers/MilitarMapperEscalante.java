package apisemaperreio.escalante.escalante.utils.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import apisemaperreio.escalante.escalante.domain.escalante.Militar;
import apisemaperreio.escalante.escalante.dtos.MilitarEscalavel;

@Mapper(componentModel = "spring")
public interface MilitarMapperEscalante {

    @Mapping(target = "ultimosServicos", ignore = true)
    Militar toMilitar(MilitarEscalavel militarEscalavel);

    List<Militar> toListMilitar(List<MilitarEscalavel> militaresEscalaveis);

}
