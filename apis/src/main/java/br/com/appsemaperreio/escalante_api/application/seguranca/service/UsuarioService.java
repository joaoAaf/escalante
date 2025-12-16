package br.com.appsemaperreio.escalante_api.application.seguranca.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.appsemaperreio.escalante_api.application.seguranca.UsuarioUseCases;
import br.com.appsemaperreio.escalante_api.application.seguranca.mappers.PerfilMapper;
import br.com.appsemaperreio.escalante_api.application.seguranca.mappers.UsuarioMapper;
import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;
import br.com.appsemaperreio.escalante_api.domain.seguranca.Usuario;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioResponse;
import br.com.appsemaperreio.escalante_api.repository.seguranca.UsuarioRepository;

@Service
public class UsuarioService implements UsuarioUseCases {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PerfilMapper perfilMapper;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PerfilMapper perfilMapper,
            PasswordEncoder passwordEncoder, Environment env) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.perfilMapper = perfilMapper;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    private void validarUsernameInicial(String username) {
        Optional.ofNullable(env.getProperty("env.usuario.inicial.username"))
                .map(u -> u.isBlank() ? null : u)
                .ifPresent(usernameInicial -> {
                    if (username.equals(usernameInicial))
                        throw new IllegalArgumentException("Username não pode ser igual ao do usuário inicial");
                });
    }

    private String obterEnvUsuario(String env, String msgErro) {
        return Optional.ofNullable(this.env.getProperty(env))
                .map(rp -> rp.isBlank() ? null : rp)
                .orElseThrow(() -> new IllegalStateException(msgErro));
    }

    @Transactional
    @Override
    public void cadastrarUsuarioInicial() {
        if (!usuarioRepository.existsByPerfisContaining(Perfil.ADMIN)) {
            var username = obterEnvUsuario(
                    "env.usuario.inicial.username",
                    "Username do usuário inicial não configurado");

            usuarioRepository.findByUsername(username)
                    .ifPresentOrElse(usuario -> {
                        usuario.getPerfis().add(Perfil.ADMIN);
                        usuarioRepository.save(usuario);
                    },
                            () -> {
                                var rawPassword = obterEnvUsuario(
                                        "env.usuario.inicial.password",
                                        "Senha do usuário inicial não configurada");

                                var password = passwordEncoder.encode(rawPassword);

                                usuarioRepository.save(new Usuario(
                                        username,
                                        password,
                                        Set.of(Perfil.ADMIN)));
                            });
        }
    }

    @Transactional
    @Override
    public UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest) {
        validarUsernameInicial(usuarioRequest.username());
        var rawPassword = obterEnvUsuario(
                "env.usuario.padrao.password",
                "Senha padrão para novos usuários não configurada");

        if (usuarioRepository.existsByUsername(usuarioRequest.username()))
            throw new IllegalArgumentException("Username já está em uso");

        var usuario = usuarioMapper.toUsuario(usuarioRequest);
        var password = passwordEncoder.encode(rawPassword);
        usuario.setPassword(password);

        return usuarioMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioResponse obterUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(usuarioMapper::toUsuarioResponse)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioResponse> listarUsuarios() {
        var usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty())
            return List.of();
        return usuarioMapper.toListUsuarioResponse(usuarios);
    }

    @Transactional
    @Override
    public String atualizarUsername(String usernameAtual, String usernameNovo) {
        validarUsernameInicial(usernameNovo);

        if (usuarioRepository.existsByUsername(usernameNovo))
            throw new IllegalArgumentException("Username já está em uso");

        var usuario = usuarioRepository.findByUsername(usernameAtual)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        usuario.setUsername(usernameNovo);

        return usuarioRepository.save(usuario).getUsername();
    }

    @Transactional
    @Override
    public void atualizarPassword(String username, String passwordNovo) {
        var usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        usuario.setPassword(passwordEncoder.encode(passwordNovo));

        usuarioRepository.save(usuario);
    }

    @Transactional
    @Override
    public List<Perfil> adicionarPerfis(UsuarioRequest usuarioRequest) {
        var perfis = perfilMapper.setStringToPerfis(usuarioRequest.perfis());

        var usuario = usuarioRepository.findByUsername(usuarioRequest.username())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (usuario.getPerfis().containsAll(perfis))
            throw new IllegalArgumentException("Usuário já possui todos os perfis informados");

        usuario.getPerfis().addAll(perfis);

        usuarioRepository.save(usuario);

        return usuario.getPerfis().stream().toList();
    }

    @Transactional
    @Override
    public List<Perfil> removerPerfis(UsuarioRequest usuarioRequest) {
        var perfis = perfilMapper.setStringToPerfis(usuarioRequest.perfis());

        var usuario = usuarioRepository.findByUsername(usuarioRequest.username())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (!usuario.getPerfis().containsAll(perfis))
            throw new IllegalArgumentException("Usuário não possui todos os perfis informados");

        if (usuario.getPerfis().stream().anyMatch(
                p -> p.equals(Perfil.ADMIN)
                        && usuarioRepository.isOnlyUserWithPerfil(usuarioRequest.username(), Perfil.ADMIN)))
            throw new IllegalArgumentException("Não é possível remover o perfil ADMIN do único usuário que o possui");

        usuario.getPerfis().removeAll(perfis);

        if (usuario.getPerfis().isEmpty())
            throw new IllegalArgumentException("O usuário deve possuir ao menos um perfil");

        usuarioRepository.save(usuario);

        return usuario.getPerfis().stream().toList();
    }

    @Transactional
    @Override
    public void deletarUsuario(String username) {
        usuarioRepository.findByUsername(username)
                .ifPresentOrElse(
                        usuario -> {
                            if (usuario.getPerfis().stream().anyMatch(
                                    p -> p.equals(Perfil.ADMIN)
                                            && usuarioRepository.isOnlyUserWithPerfil(username, Perfil.ADMIN)))
                                throw new IllegalArgumentException(
                                        "Não é possível deletar o único usuário com o perfil ADMIN");
                            usuarioRepository.delete(usuario);
                        }, () -> {
                            throw new NoSuchElementException("Usuário não encontrado");
                        });
    }

}
