package br.com.appsemaperreio.escalante_api.seguranca.model.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(max = 130, message = "O nome de usuário deve ter no máximo 130 caracteres")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "O nome de usuário deve ser um e-mail válido")
    String username,
    @NotEmpty(message = "Pelo menos um perfil deve ser atribuído ao usuário")
    Set<@NotBlank String> perfis) {

}
