package br.com.appsemaperreio.escalante_api.seguranca.application;

import java.util.List;

import br.com.appsemaperreio.escalante_api.seguranca.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.dto.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.seguranca.dto.UsuarioResponse;

public interface UsuarioUseCases {

    void cadastrarUsuarioInicial();

    UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse obterUsuarioPorUsername(String username);

    List<UsuarioResponse> listarUsuarios();

    String atualizarUsername(String usernameAtual, String usernameNovo);

    void atualizarPassword(String username, String passwordNovo);

    List<Perfil> adicionarPerfis(UsuarioRequest usuarioRequest);

    List<Perfil> removerPerfis(UsuarioRequest usuarioRequest);

    void deletarUsuario(String username);

}
