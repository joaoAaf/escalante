package br.com.appsemaperreio.escalante_api.seguranca.model.dto;

import java.util.Set;

import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;

public record UsuarioResponse(String username, Set<Perfil> perfis, String senhaGerada) {

    public UsuarioResponse(String username, Set<Perfil> perfis) { this(username, perfis, null); }

}
