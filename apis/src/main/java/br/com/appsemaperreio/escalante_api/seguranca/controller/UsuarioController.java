package br.com.appsemaperreio.escalante_api.seguranca.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.appsemaperreio.escalante_api.seguranca.model.application.IUsuarioService;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.model.dto.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.seguranca.model.dto.UsuarioResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioUseCases;

    public UsuarioController(IUsuarioService usuarioUseCases) {
        this.usuarioUseCases = usuarioUseCases;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(
            @RequestBody @Valid @NotNull(message = "O corpo da requisição é obrigatório") UsuarioRequest usuarioRequest) {
        var usuarioResponse = usuarioUseCases.cadastrarUsuario(usuarioRequest);
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuarioResponse.username())).body(usuarioResponse);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UsuarioResponse> obterUsuarioPorUsername(
            @PathVariable @NotBlank(message = "O username é obrigatório") String username) {
        var usuarioResponse = usuarioUseCases.obterUsuarioPorUsername(username);
        return ResponseEntity.ok(usuarioResponse);
    }

    @GetMapping("/atual")
    public ResponseEntity<UsuarioResponse> obterUsuarioPorUsername(Authentication authentication) {
        var username = authentication.getName();
        var usuarioResponse = usuarioUseCases.obterUsuarioPorUsername(username);
        return ResponseEntity.ok(usuarioResponse);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        var usuarios = usuarioUseCases.listarUsuarios();
        if (usuarios.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(usuarios);
    }

    @PatchMapping("/username")
    public ResponseEntity<Map<String, String>> atualizarUsername(Authentication authentication,
            @RequestParam("novo")
            @NotBlank(message = "O nome de usuário é obrigatório")
            @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres")
            @Pattern(regexp = "^(?!.*[\\s\\p{Zs}])[A-Za-z0-9_.-]+$",
                message = "O nome de usuário deve conter apenas letras ASCII, números, '.', '_' e '-', sem espaços")
            String usernameNovo) {
        var usernameAtual = authentication.getName();
        var usernameAtualizado = usuarioUseCases.atualizarUsername(usernameAtual, usernameNovo);
        return ResponseEntity.ok(Map.of("username", usernameAtualizado));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> atualizarPassword(Authentication authentication,
            @RequestParam("novo")
            @NotBlank(message = "A nova senha é obrigatória")
            @Size(min = 9, max = 50, message = "A nova senha deve ter entre 9 e 50 caracteres")
            @Pattern(regexp = "^(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\d)(?=.*\\p{Punct})[\\p{L}\\d\\p{Punct}]+$",
                message = "A nova senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial")
            String passwordNovo) {
        var username = authentication.getName();
        usuarioUseCases.atualizarPassword(username, passwordNovo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/perfis/adicionar")
    public ResponseEntity<List<Perfil>> adicionarPerfis(
            @RequestBody @Valid @NotNull(message = "O corpo da requisição é obrigatório") UsuarioRequest usuarioRequest) {
        var perfisAdicionados = usuarioUseCases.adicionarPerfis(usuarioRequest);
        return ResponseEntity.ok(perfisAdicionados);
    }

    @PatchMapping("/perfis/remover")
    public ResponseEntity<List<Perfil>> removerPerfis(
            @RequestBody @Valid @NotNull(message = "O corpo da requisição é obrigatório") UsuarioRequest usuarioRequest) {
        var perfisRemovidos = usuarioUseCases.removerPerfis(usuarioRequest);
        return ResponseEntity.ok(perfisRemovidos);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable @NotBlank(message = "O username é obrigatório") String username) {
        usuarioUseCases.deletarUsuario(username);
        return ResponseEntity.noContent().build();
    }

}