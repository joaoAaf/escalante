package br.com.appsemaperreio.escalante_api.dto.seguranca;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres")
    @Pattern(regexp = "^(?!.*[\\s\\p{Zs}])[A-Za-z0-9_.-]+$",
                message = "O nome de usuário deve conter apenas letras ASCII, números, '.', '_' e '-', sem espaços")
    String username,
    @NotEmpty(message = "Pelo menos um perfil deve ser atribuído ao usuário")
    Set<@NotBlank String> perfis) {

}
