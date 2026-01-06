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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String ADMIN = "admin@example.com";
    private static final String NORMAL = "normal@example.com";
    private static final String MULTI = "multi@example.com";

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        criarUsuario(ADMIN, "adminpass", Set.of(Perfil.ADMIN));
        criarUsuario(NORMAL, "userpass", Set.of(Perfil.ESCALANTE));
        criarUsuario(MULTI, "multipass", Set.of(Perfil.ADMIN, Perfil.ESCALANTE));
    }

    private void criarUsuario(String username, String rawPassword, Set<Perfil> perfis) {
        var u = new Usuario(username, passwordEncoder.encode(rawPassword), perfis);
        usuarioRepository.save(u);
    }

    private String login(String username, String rawPassword, boolean adminParam) throws Exception {
        var result = mockMvc.perform(post("/api/login")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, rawPassword))
                        .param("admin", String.valueOf(adminParam)))
                .andExpect(status().isOk())
                .andReturn();

        var content = result.getResponse().getContentAsString();
        var root = mapper.readTree(content);
        return root.get("bearerToken").asText();
    }

    @Test
    void semToken_deveRetornar401_paraUsuarioAtual() throws Exception {
        mockMvc.perform(get("/api/usuarios/atual"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void obterUsuarioPorUsername_adminPodeAcessar_devolve200() throws Exception {
        String token = login(ADMIN, "adminpass", true);

        mockMvc.perform(get("/api/usuarios/{username}", NORMAL).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(NORMAL));
    }

    @Test
    void obterUsuarioPorUsername_naoAdminRecebe403() throws Exception {
        String token = login(NORMAL, "userpass", false);

        mockMvc.perform(get("/api/usuarios/{username}", MULTI).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void listarUsuarios_adminRecebe200ELista() throws Exception {
        String token = login(ADMIN, "adminpass", true);

        mockMvc.perform(get("/api/usuarios").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void listarUsuarios_naoAdminRecebe403() throws Exception {
        String token = login(NORMAL, "userpass", false);

        mockMvc.perform(get("/api/usuarios").header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void atualizarUsername_adminAtualizaProprioUsername() throws Exception {
        String token = login(ADMIN, "adminpass", true);

        var novo = "admin.renamed@example.com";

        mockMvc.perform(patch("/api/usuarios/username").header("Authorization", "Bearer " + token)
                        .param("novo", novo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(novo));

        assertThat(usuarioRepository.findByUsername(novo).isPresent()).isTrue();
    }

    @Test
    void atualizarPassword_requireBasicAuth_eAtualizaSenha() throws Exception {
        var newPassword = "Newpass1!";

        mockMvc.perform(patch("/api/usuarios/password")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(NORMAL, "userpass"))
                        .param("novo", newPassword))
                .andExpect(status().isNoContent());

        var usuario = usuarioRepository.findByUsername(NORMAL).orElseThrow();
        assertThat(passwordEncoder.matches(newPassword, usuario.getPassword())).isTrue();
        assertThat(usuario.isSenhaTemporaria()).isFalse();
    }

    @Test
    void adicionarPerfis_adminAdicionaPerfil() throws Exception {
        String token = login(ADMIN, "adminpass", true);

        var payload = mapper.createObjectNode();
        payload.put("username", NORMAL);
        payload.putArray("perfis").add("ADMIN");

        mockMvc.perform(patch("/api/usuarios/perfis/adicionar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        var usuario = usuarioRepository.findByUsername(NORMAL).orElseThrow();
        assertThat(usuario.getPerfis()).contains(Perfil.ADMIN);
    }

    @Test
    void removerPerfis_adminRemovePerfil() throws Exception {
        String token = login(ADMIN, "adminpass", true);

        var payload = mapper.createObjectNode();
        payload.put("username", MULTI);
        payload.putArray("perfis").add("ESCALANTE");

        mockMvc.perform(patch("/api/usuarios/perfis/remover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        var usuario = usuarioRepository.findByUsername(MULTI).orElseThrow();
        assertThat(usuario.getPerfis()).doesNotContain(Perfil.ESCALANTE);
    }

    @Test
    void deletarUsuario_adminDeletaUsuario() throws Exception {
        String token = login(ADMIN, "adminpass", true);

        mockMvc.perform(delete("/api/usuarios/{username}", NORMAL).header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        assertThat(usuarioRepository.findByUsername(NORMAL).isPresent()).isFalse();
    }

}
