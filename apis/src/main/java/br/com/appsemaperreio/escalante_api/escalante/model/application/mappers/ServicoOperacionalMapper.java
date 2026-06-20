package br.com.appsemaperreio.escalante_api.escalante.model.application.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.OperadorLinha;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.ServicoOperacional;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;

@Mapper(componentModel = "spring")
public interface ServicoOperacionalMapper {

    default ServicoOperacionalDto toServicoOperacionalDto(ServicoOperacional servicoOperacional) {
        if (servicoOperacional == null)
            return null;
        Militar militar = servicoOperacional.getMilitar();
        String matricula = militar != null ? militar.getMatricula() : null;
        String nomePaz = militar != null ? militar.getNomePaz() : null;
        String patente = (militar != null && militar.getPatente() != null) ? militar.getPatente().name() : null;
        String funcao = servicoOperacional.getFuncao() != null ? servicoOperacional.getFuncao().getNome() : null;
        Integer linha = (servicoOperacional instanceof OperadorLinha ol) ? ol.getLinha() : null;
        return new ServicoOperacionalDto(
                servicoOperacional.getDataServico(),
                matricula,
                nomePaz,
                patente,
                funcao,
                linha);
    }

    default List<ServicoOperacionalDto> toListServicoOperacionalDto(List<ServicoOperacional> servicosEscala) {
        if (servicosEscala == null)
            return null;
        List<ServicoOperacionalDto> lista = new ArrayList<>(servicosEscala.size());
        for (ServicoOperacional servico : servicosEscala) {
            lista.add(toServicoOperacionalDto(servico));
        }
        return lista;
    }

}
