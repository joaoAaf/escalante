package br.com.appsemaperreio.escalante_api.escalante.integracao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.domain.escalante.Funcao;
import br.com.appsemaperreio.escalante_api.utils.adapters.importador_xlsx.ImportadorEscalaXLSXAdapter;
import br.com.appsemaperreio.escalante_api.utils.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;

@SpringBootTest
class ImportadorXLSXIntegrationTest {

	@Autowired
	private ImportadorMilitaresXLSXAdapter importadorMilitaresXLSXAdapter;

	@Autowired
	private ImportadorEscalaXLSXAdapter importadorEscalaXLSXAdapter;

	@Test
	public void deveImportarMilitaresXLSXQuandoPlanilhaPresenteEValida() throws Exception {
		// Dados
		final int QUANTIDADE_MILITARES_ESCALAVEIS = 25;
		final String MATRICULA_MILITAR_FOLGA_ESPECIAL = "REG835";
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
		final int QUANTIDADE_SERVICOS_OPERACIONAIS = 156;
		final LocalDate DATA_19_09_2024 = LocalDate.of(2024, 9, 19);
		final String MATRICULA_MILITAR_COV_19_09_2024 = "REG273";

		var inputStream = getClass().getResourceAsStream("/samples/modelo_importacao_escala.xlsx");

		MultipartFile multipartFile = new MockMultipartFile(
				"file", "modelo_importacao_escala.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				inputStream);

		// Quando
		var servicosOperacionais = importadorEscalaXLSXAdapter.importarEscalaXLSX(multipartFile);
		var servicoCov19092024 = servicosOperacionais.stream()
				.filter(servico -> servico.dataServico().isEqual(DATA_19_09_2024) && servico.funcao().equals(Funcao.COV.getNome()))
				.findFirst();
		
		// Então
		assertEquals(QUANTIDADE_SERVICOS_OPERACIONAIS, servicosOperacionais.size());
		assertTrue(servicoCov19092024.isPresent());
		assertEquals(MATRICULA_MILITAR_COV_19_09_2024, servicoCov19092024.get().matricula());
	}

}
