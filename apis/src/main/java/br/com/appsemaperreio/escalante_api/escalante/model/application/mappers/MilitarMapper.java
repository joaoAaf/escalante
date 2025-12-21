package br.com.appsemaperreio.escalante_api.escalante.model.application.mappers;

import java.util.List;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;

@Mapper(componentModel = "spring")
public interface MilitarMapper {

    @Mapping(target = "ultimosServicos", ignore = true)
    Militar toMilitar(MilitarDto militarDto);

    MilitarDto toMilitarDto(Militar militar);

    List<Militar> toListMilitar(List<MilitarDto> militaresDto);

    List<MilitarDto> toListMilitarDto(List<Militar> militares);

}
