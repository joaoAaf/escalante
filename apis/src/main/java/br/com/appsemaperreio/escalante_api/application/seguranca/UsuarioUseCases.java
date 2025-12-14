package br.com.appsemaperreio.escalante_api.application.seguranca;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioResponse;

public interface UsuarioUseCases {

    void cadastrarUsuarioInicial();

    UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse obterUsuarioPorUsername(String username);

    UsuarioResponse obterUsuarioPorToken(String token);

    List<UsuarioResponse> listarUsuarios();

    String atualizarUsername(Authentication authentication, String usernameNovo);

    void atualizarPassword(Authentication authentication, String novaPassword);

    List<Perfil> adicionarPerfis(String username, Set<String> perfis);

    void removerPerfis(String username, Set<String> perfis);

    void deletarUsuario(String username);

}
