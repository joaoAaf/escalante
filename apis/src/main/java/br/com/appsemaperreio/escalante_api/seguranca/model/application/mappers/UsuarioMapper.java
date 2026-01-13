package br.com.appsemaperreio.escalante_api.seguranca.model.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Usuario;
import br.com.appsemaperreio.escalante_api.seguranca.model.dto.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.seguranca.model.dto.UsuarioResponse;

@Mapper(componentModel = "spring", uses = { PerfilMapper.class })
public interface UsuarioMapper {

    List<UsuarioResponse> toListUsuarioResponse(List<Usuario> usuarios);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "perfis", source = "perfis", qualifiedByName = "setStringToPerfis")
    Usuario toUsuario(UsuarioRequest usuarioRequest);

    @Mapping(target = "senhaGerada", ignore = true)
    UsuarioResponse toUsuarioResponse(Usuario usuario);

}
