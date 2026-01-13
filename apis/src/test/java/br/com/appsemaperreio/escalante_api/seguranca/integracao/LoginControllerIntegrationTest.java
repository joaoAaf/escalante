package br.com.appsemaperreio.escalante_api.seguranca.integracao;

import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Usuario;
import br.com.appsemaperreio.escalante_api.seguranca.model.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        var user1 = new Usuario("testuser1", passwordEncoder.encode("pass123"), Set.of(Perfil.ADMIN));
        var user2 = new Usuario("testuser2", passwordEncoder.encode("pass456"), Set.of(Perfil.ADMIN, Perfil.ESCALANTE));
        var user3 = new Usuario("testuser3", passwordEncoder.encode("pass789"), Set.of(Perfil.ESCALANTE));
        user3.setSenhaTemporaria(true);
        usuarioRepository.save(user1);
        usuarioRepository.save(user2);
        usuarioRepository.save(user3);
    }

    @Test
    void deveRetornarTokenAoFazerLoginCredenciaisValidos() throws Exception {
        mockMvc.perform(post("/api/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser1", "pass123"))
                .param("admin", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bearerToken").exists());
    }

    @Test
    void deveRetortar401AoFazerLoginComCredenciaisInvalidas() throws Exception {
        mockMvc.perform(post("/api/login").with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser1", "wrongpass"))
                .param("admin", "true"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarTokenComPerfilCorreto() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/login")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser2", "pass456"))
                .param("admin", "false"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        var root = mapper.readTree(content);
        String token = root.get("bearerToken").asText();

        // Decodifica o payload do JWT (sem validar assinatura) e obtém o claim 'scope'
        String[] parts = token.split("\\.");
        assertTrue(parts.length >= 2);
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        var payload = mapper.readTree(payloadJson);

        List<String> scopes = new ArrayList<>();
        if (payload.has("scope")) {
            var scopeNode = payload.get("scope");
            if (scopeNode.isArray()) {
                for (var n : scopeNode) scopes.add(n.asText());
            } else {
                scopes.add(scopeNode.asText());
            }
        }

        // Verifica que o token contém apenas o perfil 'ESCALANTE'
        assertEquals(List.of("ESCALANTE"), scopes);
    }

    @Test
    void deveRetornarTokenVazioQuandoSenhaPadraoOuInicial() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/login")
                .with(SecurityMockMvcRequestPostProcessors.httpBasic("testuser3", "pass789"))
                .param("admin", "false"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        var root = mapper.readTree(content);
        String token = root.get("bearerToken").asText();

        // quando a senha é temporária, o login deve retornar uma string vazia
        assertEquals("", token);
    }

}
