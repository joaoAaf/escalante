package br.com.appsemaperreio.escalante_api.seguranca.model.dto;

import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record UsuarioResponse(String username, Set<Perfil> perfis, String senhaGerada) {

    public UsuarioResponse(String username, Set<Perfil> perfis) { this(username, perfis, null); }

}
