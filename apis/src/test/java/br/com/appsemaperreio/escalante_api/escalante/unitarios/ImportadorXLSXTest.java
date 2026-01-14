package br.com.appsemaperreio.escalante_api.escalante.unitarios;

import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorEscalaXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.apachepoi.ImportadorEscalaXLSXApachePoi;
import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.apachepoi.ImportadorMilitaresXLSXApachePoi;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportadorXLSXTest {

    private ImportadorMilitaresXLSXAdapter importadorMilitaresXLSXAdapter;
    private ImportadorEscalaXLSXAdapter importadorEscalaXLSXAdapter;

    @BeforeEach
    void setup() {
        this.importadorMilitaresXLSXAdapter = new ImportadorMilitaresXLSXApachePoi();
        this.importadorEscalaXLSXAdapter = new ImportadorEscalaXLSXApachePoi();
    }

    @Test
    public void deveImportarMilitaresXLSXQuandoPlanilhaPresenteEValida() throws Exception {
        // Dados
        final int QUANTIDADE_MILITARES_ESCALAVEIS = 25;
        final String MATRICULA_MILITAR_FOLGA_ESPECIAL = "REG00011";
        final int FOLGA_ESPECIAL = 7;

        var inputStream = getClass().getResourceAsStream("/samples/modelo_importacao_militares.xlsx");

        MultipartFile multipartFile = new MockMultipartFile(
                "file", "modelo_importacao_militares.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                inputStream);


        // Quando
        var militaresEscalaveis = importadorMilitaresXLSXAdapter.importarMilitaresXLSX(multipartFile);
        var militarFolgaEspecial = militaresEscalaveis.stream()
                .filter(militar -> militar.matricula().equals(MATRICULA_MILITAR_FOLGA_ESPECIAL)).findFirst();

        // Então
        assertEquals(QUANTIDADE_MILITARES_ESCALAVEIS, militaresEscalaveis.size());
        assertTrue(militarFolgaEspecial.isPresent());
        assertEquals(FOLGA_ESPECIAL, militarFolgaEspecial.get().folgaEspecial());
    }

    @Test
    public void deveImportarEscalaXLSXQuandoPlanilhaPresenteEValida() throws Exception {
        // Dados
        final int QUANTIDADE_SERVICOS_OPERACIONAIS = 171;
        final LocalDate DATA_19_01_2026 = LocalDate.of(2026, 1, 19);
        final String MATRICULA_MILITAR_COV_19_01_2026 = "REG00008";

        var inputStream = getClass().getResourceAsStream("/samples/modelo_importacao_escala.xlsx");

        MultipartFile multipartFile = new MockMultipartFile(
                "file", "modelo_importacao_escala.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                inputStream);

        // Quando
        var servicosOperacionais = importadorEscalaXLSXAdapter.importarEscalaXLSX(multipartFile);
        var servicoCov19012026 = servicosOperacionais.stream()
                .filter(servico -> servico.dataServico().isEqual(DATA_19_01_2026) &&
                        servico.funcao().equals(Funcao.COV.getNome()))
                .findFirst();

        // Então
        assertEquals(QUANTIDADE_SERVICOS_OPERACIONAIS, servicosOperacionais.size());
        assertTrue(servicoCov19012026.isPresent());
        assertEquals(MATRICULA_MILITAR_COV_19_01_2026, servicoCov19012026.get().matricula());
    }

}
