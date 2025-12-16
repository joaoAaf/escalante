package br.com.appsemaperreio.escalante_api.seguranca.dto;

import java.util.Set;

import br.com.appsemaperreio.escalante_api.seguranca.domain.Perfil;

public record UsuarioResponse(String username, Set<Perfil> perfis) {

}
