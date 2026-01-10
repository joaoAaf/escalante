package br.com.appsemaperreio.escalante_api.escalante.integracao;

import br.com.appsemaperreio.escalante_api.escalante.model.repository.MilitarRepository;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Usuario;
import br.com.appsemaperreio.escalante_api.seguranca.model.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class MilitarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MilitarRepository militarRepository;

    @Autowired
    private ObjectMapper mapper;

    private static final String ESCALANTE = "escalante@example.com";

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
        militarRepository.deleteAll();

        var usuario = new Usuario(ESCALANTE, passwordEncoder.encode("pass123"), Set.of(Perfil.ESCALANTE));
        usuarioRepository.save(usuario);
    }

    private String login(String username, String rawPassword, boolean adminParam) throws Exception {
        var result = mockMvc.perform(post("/api/login")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(username, rawPassword))
                        .param("admin", String.valueOf(adminParam)))
                .andExpect(status().isOk())
                .andReturn();

        var content = result.getResponse().getContentAsString();
        JsonNode root = mapper.readTree(content);
        return root.get("bearerToken").asText();
    }

    @Test
    void semToken_deveRetornar401_paraObterModeloXlsx() throws Exception {
        mockMvc.perform(get("/api/militar/modelo/xlsx"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void obterPlanilhaModeloMilitares_deveRetornarXlsxValido() throws Exception {
        String token = login(ESCALANTE, "pass123", false);

        MvcResult result = mockMvc.perform(get("/api/militar/modelo/xlsx")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        var response = result.getResponse();
        String contentType = response.getContentType();
        String contentDisposition = response.getHeader("Content-Disposition");
        byte[] body = response.getContentAsByteArray();

        // Verificações de headers e conteúdo
        assertThat(contentType).isEqualTo("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        assertThat(contentDisposition).contains("modelo_importacao_militares.xlsx");
        assertThat(body).isNotNull();
        assertThat(body.length).isGreaterThan(0);

        // Tentar abrir como workbook Apache POI para garantir que é um xlsx válido
        try (var bis = new ByteArrayInputStream(body); Workbook wb = WorkbookFactory.create(bis)) {
            assertThat(wb).isNotNull();
            assertThat(wb.getNumberOfSheets()).isGreaterThan(0);
        }
    }


    @Test
    void importarMilitaresXLSX_deveRetornarLista() throws Exception {
        String token = login(ESCALANTE, "pass123", false);

        InputStream is = getClass().getResourceAsStream("/samples/modelo_importacao_militares.xlsx");
        assertThat(is).isNotNull();

        MockMultipartFile file = new MockMultipartFile(
                "militares",
                "modelo_importacao_militares.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                is);

        mockMvc.perform(multipart("/api/militar/importar/xlsx")
                        .file(file)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(25));
    }

    @Test
    void listarMilitares_deveRetornarListaEConterMilitar() throws Exception {
        String token = login(ESCALANTE, "pass123", false);

        // criar 3 militares para listagem
        ObjectNode a = mapper.createObjectNode();
        a.put("antiguidade", 2);
        a.put("matricula", "L0000001");
        a.put("patente", "CB");
        a.put("nomePaz", "Lista Um");
        a.put("nascimento", "1990-01-01");
        a.put("folgaEspecial", 0);
        a.put("cov", false);

        ObjectNode b = mapper.createObjectNode();
        b.put("antiguidade", 3);
        b.put("matricula", "L0000002");
        b.put("patente", "SD");
        b.put("nomePaz", "Lista Dois");
        b.put("nascimento", "1991-02-02");
        b.put("folgaEspecial", 1);
        b.put("cov", true);

        ObjectNode c = mapper.createObjectNode();
        c.put("antiguidade", 1);
        c.put("matricula", "L0000003");
        c.put("patente", "SGT");
        c.put("nomePaz", "Lista Tres");
        c.put("nascimento", "1992-03-03");
        c.put("folgaEspecial", 2);
        c.put("cov", false);

        ArrayNode payload = mapper.createArrayNode();
        payload.add(a);
        payload.add(b);
        payload.add(c);

        mockMvc.perform(post("/api/militar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        MvcResult listRes = mockMvc.perform(get("/api/militar").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode listed = mapper.readTree(listRes.getResponse().getContentAsString());
        assertThat(listed.isArray()).isTrue();
        assertThat(listed.size()).isGreaterThanOrEqualTo(3);

        boolean found = false;
        for (JsonNode n : listed)
            if (n.has("matricula") && n.get("matricula").asText().equals("L0000002")) {
                found = true;
                break;
            }
        assertThat(found).isTrue();
    }

    @Test
    void cadastrarMilitares_deveSalvarEConterMesmoJson() throws Exception {
        String token = login(ESCALANTE, "pass123", false);

        String mat1 = "C0000001";
        String mat2 = "C0000002";
        String mat3 = "C0000003";

        ObjectNode m1 = mapper.createObjectNode();
        m1.put("antiguidade", 2);
        m1.put("matricula", mat1);
        m1.put("patente", "CB");
        m1.put("nomePaz", "Cadastrar Um");
        m1.put("nascimento", "1990-01-01");
        m1.put("folgaEspecial", 0);
        m1.put("cov", false);

        ObjectNode m2 = mapper.createObjectNode();
        m2.put("antiguidade", 0);
        m2.put("matricula", mat2);
        m2.put("patente", "SD");
        m2.put("nomePaz", "Cadastrar Dois");
        m2.put("nascimento", "1991-02-02");
        m2.put("folgaEspecial", 1);
        m2.put("cov", true);

        ObjectNode m3 = mapper.createObjectNode();
        m3.put("antiguidade", 1);
        m3.put("matricula", mat3);
        m3.put("patente", "SGT");
        m3.put("nomePaz", "Cadastrar Tres");
        m3.put("nascimento", "1992-03-03");
        m3.put("folgaEspecial", 2);
        m3.put("cov", false);

        ArrayNode payload = mapper.createArrayNode();
        payload.add(m1);
        payload.add(m2);
        payload.add(m3);

        MvcResult res = mockMvc.perform(post("/api/militar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        // ajustar antiguidades esperadas (de acordo com a regra de negócio)
        m2.put("antiguidade", 3);

        JsonNode created = mapper.readTree(res.getResponse().getContentAsString());
        assertThat(created.isArray()).isTrue();
        assertThat(created.size()).isEqualTo(3);

        // montar mapa por matrícula e verificar igualdade
        Map<String, JsonNode> map = new HashMap<>();
        for (JsonNode n : created) map.put(n.get("matricula").asText(), n);

        // verificar presença dos ids (matrículas)
        assertThat(map.containsKey(mat1)).isTrue();
        assertThat(map.containsKey(mat2)).isTrue();
        assertThat(map.containsKey(mat3)).isTrue();

        // Comparar json retornado com o enviado
        assertThat(map.get(mat1)).isEqualTo(m1);
        assertThat(map.get(mat2)).isEqualTo(m2);
        assertThat(map.get(mat3)).isEqualTo(m3);
    }

    @Test
    void cadastrarMilitares_comMatriculasDuplicadas_deveRetornar400() throws Exception {
        String token = login(ESCALANTE, "pass123", false);

        String mat1 = "C0000001";

        ObjectNode m1 = mapper.createObjectNode();
        m1.put("antiguidade", 1);
        m1.put("matricula", mat1);
        m1.put("patente", "CB");
        m1.put("nomePaz", "Cadastrar Um");
        m1.put("nascimento", "1990-01-01");
        m1.put("folgaEspecial", 0);
        m1.put("cov", false);

        ObjectNode m2 = mapper.createObjectNode();
        m2.put("antiguidade", 0);
        m2.put("matricula", mat1); // Mesma matrícula
        m2.put("patente", "SD");
        m2.put("nomePaz", "Cadastrar Dois");
        m2.put("nascimento", "1991-02-02");
        m2.put("folgaEspecial", 1);
        m2.put("cov", true);

        ArrayNode militares = mapper.createArrayNode();
        militares.add(m1);
        militares.add(m2);

        String json = mapper.writeValueAsString(militares);

        mockMvc.perform(post("/api/militar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Mensagem").value("Existem matrículas duplicadas na lista de militares a serem cadastrados."));
    }

    @Test
    void cadastrarMilitares_comAntiguidadesDuplicadas_deveRetornar400() throws Exception {
        String token = login(ESCALANTE, "pass123", false);

        ObjectNode m1 = mapper.createObjectNode();
        m1.put("antiguidade", 1);
        m1.put("matricula", "C0000001");
        m1.put("patente", "CB");
        m1.put("nomePaz", "Cadastrar Um");
        m1.put("nascimento", "1990-01-01");
        m1.put("folgaEspecial", 0);
        m1.put("cov", false);

        ObjectNode m2 = mapper.createObjectNode();
        m2.put("antiguidade", 1); // Mesma antiguidade
        m2.put("matricula", "C0000002");
        m2.put("patente", "SD");
        m2.put("nomePaz", "Cadastrar Dois");
        m2.put("nascimento", "1991-02-02");
        m2.put("folgaEspecial", 1);
        m2.put("cov", true);

        ArrayNode militares = mapper.createArrayNode();
        militares.add(m1);
        militares.add(m2);

        String json = mapper.writeValueAsString(militares);

        mockMvc.perform(post("/api/militar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Mensagem").value("Existem antiguidades duplicadas na lista de militares a serem cadastrados."));
    }

    @Test
    void atualizarMilitar_deveSalvarEConterMesmoJson() throws Exception {
        // Primeiro, cadastra um militar para depois atualizar
        cadastrarMilitares_deveSalvarEConterMesmoJson();

        String token = login(ESCALANTE, "pass123", false);
        String mat1 = "C0000002";

        ObjectNode m1 = mapper.createObjectNode();
        m1.put("antiguidade", 2); // Alterando antiguidade de 3 para 2
        m1.put("matricula", mat1);
        m1.put("patente", "CB"); // Alterando patente de SD para CB
        m1.put("nomePaz", "Atualizado Um"); // Alterando nomePaz
        m1.put("nascimento", "1991-02-02");
        m1.put("folgaEspecial", 1);
        m1.put("cov", true);

        String json = mapper.writeValueAsString(m1);

        mockMvc.perform(put("/api/militar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/militar/" + mat1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricula").value(mat1))
                .andExpect(jsonPath("$.antiguidade").value(2))
                .andExpect(jsonPath("$.patente").value("CB"))
                .andExpect(jsonPath("$.nomePaz").value("Atualizado Um"));
    }

    @Test
    void atualizarMilitar_comDadosIguais_deveRetornar400() throws Exception {
        cadastrarMilitares_deveSalvarEConterMesmoJson();

        String token = login(ESCALANTE, "pass123", false);
        String mat1 = "C0000001";

        ObjectNode m1 = mapper.createObjectNode();
        m1.put("antiguidade", 2);
        m1.put("matricula", mat1);
        m1.put("patente", "CB");
        m1.put("nomePaz", "Cadastrar Um");
        m1.put("nascimento", "1990-01-01");
        m1.put("folgaEspecial", 0);
        m1.put("cov", false);

        String json = mapper.writeValueAsString(m1);

        mockMvc.perform(put("/api/militar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Mensagem").value("Não foram identificadas alterações no militar informado."));
    }

    @Test
    void atualizarMilitar_inexistente_deveRetornar400() throws Exception {
        String token = login(ESCALANTE, "pass123", false);
        String mat1 = "C0000009"; // Matrícula inexistente

        ObjectNode m1 = mapper.createObjectNode();
        m1.put("antiguidade", 2);
        m1.put("matricula", mat1);
        m1.put("patente", "CB");
        m1.put("nomePaz", "Cadastrar Um");
        m1.put("nascimento", "1990-01-01");
        m1.put("folgaEspecial", 0);
        m1.put("cov", true);

        String json = mapper.writeValueAsString(m1);

        mockMvc.perform(put("/api/militar")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.Mensagem").value("Militar com matrícula " + mat1 + " não encontrado."));
    }
}
