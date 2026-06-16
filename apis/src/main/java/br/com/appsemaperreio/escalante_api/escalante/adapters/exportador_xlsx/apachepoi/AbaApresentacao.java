package br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx.apachepoi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Patente;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;

public class AbaApresentacao {

    private static final List<ApresentacaoFuncao> APRESENTACOES = List.of(
            new ApresentacaoFuncao("OFICIAL/FISCAL DE DIA", 1, Funcao.FISCAL_DE_DIA, null),
            new ApresentacaoFuncao("C.O.V – ABT", 2, Funcao.COV, null),
            new ApresentacaoFuncao("OP. LINHA 01/ SOCORR", 3, Funcao.OPERADOR_DE_LINHA, 1),
            new ApresentacaoFuncao("AJUD. L 1/SOCORR.", 4, Funcao.AJUDANTE_DE_LINHA, null),
            new ApresentacaoFuncao("OP. LINHA 02/ SOCORR", 5, Funcao.OPERADOR_DE_LINHA, 2),
            new ApresentacaoFuncao("PERMANENTE", 6, Funcao.PERMANENTE, null));

    private static final String CELULA_VAZIA = "----------------------";

    private XSSFSheet planilha;
    private int numeroLinha;
    private XSSFRow linha;
    private List<String> diasSemana;
    private List<LocalDate> datasServicos;
    private int indiceDataServico;
    private int contadorDias;

    public AbaApresentacao(XSSFWorkbook workbook, List<ServicoOperacionalDto> servicos) {
        this.planilha = workbook.createSheet("Apresentação");
        this.numeroLinha = 0;
        this.diasSemana = listarDiasSemana();
        this.datasServicos = servicos.stream().map(ServicoOperacionalDto::dataServico).distinct().toList();
        this.indiceDataServico = 0;
    }

    private List<String> listarDiasSemana() {
        var diasSemana = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            diasSemana.add(DayOfWeek.of(i == 0 ? 7 : i)
                    .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pt-BR")).toUpperCase());
        }
        return diasSemana;
    }

    public void criarAbaApresentacao(XSSFWorkbook workbook, List<ServicoOperacionalDto> servicos) {
        var estilos = new EstilosPlanilha(workbook);

        while (this.indiceDataServico < this.datasServicos.size()) {
            this.criarCabecalhoAbaApresentacao(estilos.getEstiloCabecalho1(), estilos.getEstiloCabecalho2());
            this.criarLinhasAbaApresentacao(estilos.getEstiloPadrao(), servicos);
        }
    }

    private void criarCabecalhoAbaApresentacao(XSSFCellStyle estilo1, XSSFCellStyle estilo2) {
        this.linha = this.planilha.createRow(this.numeroLinha++);
        var celulaAbaixo = new Celula(this.planilha.createRow(this.numeroLinha++), estilo2);

        var verificacao = this.indiceDataServico == 0;
        var celula = new Celula(this.linha, estilo1);
        celula.incluirNaCelula(celula.criarCelula(0), verificacao ? "Data de Criação" : "");
        celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(0), verificacao ? LocalDate.now() : "");

        this.contadorDias = 0;
        for (var dia : this.diasSemana) {
            var indiceDia = this.diasSemana.indexOf(dia);
            if (this.indiceDataServico >= this.datasServicos.size()) {
                celula.incluirNaCelula(celula.criarCelula(indiceDia + 1), "");
                celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(indiceDia + 1), "");
                continue;
            }
            this.contadorDias++;
            var dataServico = this.datasServicos.get(this.indiceDataServico);
            celula.incluirNaCelula(celula.criarCelula(indiceDia + 1), dia);
            celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(indiceDia + 1), dataServico);
            this.indiceDataServico++;
        }
    }

    private void criarLinhasAbaApresentacao(XSSFCellStyle estilo, List<ServicoOperacionalDto> servicos) {

        var apresentacoes = APRESENTACOES.stream()
                .sorted(Comparator.comparingInt(ApresentacaoFuncao::ordemExibicao))
                .toList();

        for (var apresentacao : apresentacoes) {
            this.linha = this.planilha.createRow(this.numeroLinha++);
            var celula = new Celula(this.linha, estilo);
            celula.incluirNaCelula(celula.criarCelula(0), apresentacao.label());

            var indiceDataServico = this.indiceDataServico - this.contadorDias;
            var indiceColuna = 1;
            for (; indiceDataServico < this.indiceDataServico; indiceDataServico++) {
                var dataServico = this.datasServicos.get(indiceDataServico);
                var servico = buscarServico(servicos, apresentacao, dataServico);
                if (servico.isPresent())
                    celula.incluirNaCelula(celula.criarCelula(indiceColuna), formatarMilitar(servico.get()));
                else
                    celula.incluirNaCelula(celula.criarCelula(indiceColuna), CELULA_VAZIA);
                indiceColuna++;
            }
            if (this.indiceDataServico >= this.datasServicos.size()) {
                var celulasVazias = diasSemana.size() - this.contadorDias;
                for (int contador = 0; contador < celulasVazias; contador++) {
                    celula.incluirNaCelula(celula.criarCelula(indiceColuna), CELULA_VAZIA);
                    indiceColuna++;
                }
            }
        }
    }

    private Optional<ServicoOperacionalDto> buscarServico(
            List<ServicoOperacionalDto> servicos, ApresentacaoFuncao apresentacao, LocalDate dataServico) {
        return servicos.stream()
                .filter(s -> s.funcao().equals(apresentacao.funcao().getNome()))
                .filter(s -> s.dataServico().equals(dataServico))
                .filter(s -> correspondeLinha(apresentacao, s))
                .findFirst();
    }

    private boolean correspondeLinha(ApresentacaoFuncao apresentacao, ServicoOperacionalDto servico) {
        if (apresentacao.linha() == null)
            return servico.linha() == null;
        if (servico.linha() == null)
            return apresentacao.linha() == 1;
        return Objects.equals(apresentacao.linha(), servico.linha());
    }

    private String formatarMilitar(ServicoOperacionalDto servico) {
        var patente = Arrays.stream(Patente.values())
                .filter(p -> p.name().equals(servico.patente()))
                .findFirst()
                .orElseThrow();
        return patente.name() + " " + servico.nomePaz();
    }

}
