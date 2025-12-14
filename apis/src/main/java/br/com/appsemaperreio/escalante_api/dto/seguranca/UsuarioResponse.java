package br.com.appsemaperreio.escalante_api.dto.seguranca;

import java.util.Set;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;

public record UsuarioResponse(String username, Set<Perfil> perfis) {

}
