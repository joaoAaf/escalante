package br.com.appsemaperreio.escalante_api.application.seguranca;

import java.util.List;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioResponse;

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
