package br.com.appsemaperreio.escalante_api.seguranca.integracao;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Perfil;
import br.com.appsemaperreio.escalante_api.domain.seguranca.Usuario;
import br.com.appsemaperreio.escalante_api.repository.seguranca.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        var user = new Usuario("testuser", passwordEncoder.encode("pass123"), Set.of(Perfil.ADMIN));
        usuarioRepository.save(user);
    }

    @Test
    void deveRetornarTokenAoFazerLoginComPerfilECredenciaisValidos() throws Exception {
        mockMvc.perform(post("/api/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser", "pass123"))
                .param("perfil", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").exists());
    }

    @Test
    void deveRetortar401AoFazerLoginComCredenciaisInvalidas() throws Exception {
        mockMvc.perform(post("/api/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser", "wrongpass"))
                .param("perfil", "ADMIN"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornar403AoFazerLoginComPerfilInvalido() throws Exception {
        mockMvc.perform(post("/api/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser", "pass123"))
                .param("perfil", "ESCALANTE"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.Mensagem", containsString("Acesso negado")));
    }

}
