package br.com.appsemaperreio.escalante_api.seguranca.model.application;

import java.util.List;

import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.model.dto.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.seguranca.model.dto.UsuarioResponse;

public interface IUsuarioService {

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
