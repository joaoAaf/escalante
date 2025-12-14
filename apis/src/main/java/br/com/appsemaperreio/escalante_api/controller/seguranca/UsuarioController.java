package br.com.appsemaperreio.escalante_api.controller.seguranca;

import java.net.URI;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.appsemaperreio.escalante_api.application.seguranca.UsuarioUseCases;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioRequest;
import br.com.appsemaperreio.escalante_api.dto.seguranca.UsuarioResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioUseCases usuarioUseCases;

    public UsuarioController(UsuarioUseCases usuarioUseCases) {
        this.usuarioUseCases = usuarioUseCases;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(
            @RequestBody @Valid @NotNull(message = "O corpo da requisição é obrigatório") UsuarioRequest usuarioRequest) {
        var usuarioResponse = usuarioUseCases.cadastrarUsuario(usuarioRequest);
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuarioResponse.username())).body(usuarioResponse);
    }

    @PatchMapping("/username")
    public ResponseEntity<Map<String, String>> atualizarUsername(Authentication authentication,
            @RequestParam("novo") @NotBlank(message = "O nome de usuário é obrigatório") @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres") @Pattern(regexp = "^(?!.*[\\s\\p{Zs}])[A-Za-z0-9_.-]+$", message = "O nome de usuário deve conter apenas letras ASCII, números, '.', '_' e '-', sem espaços") String usernameNovo) {
        var usernameAtualizado = usuarioUseCases.atualizarUsername(authentication, usernameNovo);
        return ResponseEntity.ok(Map.of("username", usernameAtualizado));
    }

}
