package br.com.appsemaperreio.escalante_api.utils.adapters.exportador_xlsx.apachepoi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.appsemaperreio.escalante_api.domain.escalante.Funcao;
import br.com.appsemaperreio.escalante_api.domain.escalante.Patente;
import br.com.appsemaperreio.escalante_api.dtos.escalante.ServicoOperacionalDto;

public class AbaApresentacao {

    private XSSFSheet planilha;
    private int numeroLinha;
    private XSSFRow linha;
    private XSSFRow proxLinha;
    private List<String> diasSemana;
    private List<LocalDate> datasServicos;
    private int indiceDataServico;
    private int contadorDias;

    public AbaApresentacao(XSSFWorkbook workbook, List<ServicoOperacionalDto> servicos) {
        this.planilha = workbook.createSheet("Apresentação");
        this.numeroLinha = 0;
        this.diasSemana = listarDiasSemana();
        this.datasServicos = servicos.stream().map(servico -> servico.dataServico()).distinct().toList();
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
        this.proxLinha = this.planilha.createRow(this.numeroLinha++);

        var celula = new Celula(this.linha, estilo1);
        var celulaAbaixo = new Celula(this.proxLinha, estilo2);

        this.criarColunaInicialCabecalho(celula, celulaAbaixo);
        this.criarColunasPadraoCabecalho(celula, celulaAbaixo);
    }

    private void criarColunaInicialCabecalho(Celula celula, Celula celulaAbaixo) {
        var verificacao = this.indiceDataServico == 0;
        celula.incluirNaCelula(celula.criarCelula(0), verificacao ? "Data de Criação" : "");
        celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(0), verificacao ? LocalDate.now() : "");
    }

    private void criarColunasPadraoCabecalho(Celula celula, Celula celulaAbaixo) {
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

        var funcoes = Arrays.stream(Funcao.values())
                .sorted(Comparator.comparingInt(Funcao::getOrdemExibicao))
                .toList();

        for (var funcao : funcoes) {
            this.linha = this.planilha.createRow(this.numeroLinha++);
            var celula = new Celula(this.linha, estilo);
            var celulaAbaixo = new Celula(this.proxLinha, estilo);
            celula.incluirNaCelula(celula.criarCelula(0), funcao.getNome());
            if (funcao.equals(Funcao.OPERADOR_DE_LINHA)) {
                this.proxLinha = this.planilha.createRow(this.numeroLinha++);
                celulaAbaixo.linha = this.proxLinha;
                celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(0), funcao.getNome());
            }
            var indiceDataServico = this.indiceDataServico - this.contadorDias;
            var indiceColuna = 1;
            for (; indiceDataServico < this.indiceDataServico; indiceDataServico++) {
                var dataServico = this.datasServicos.get(indiceDataServico);
                var servico = servicos.stream()
                        .filter(s -> s.funcao().equals(funcao.getNome()) && s.dataServico().equals(dataServico)).toList();
                if (!servico.isEmpty()) {
                    Function<Integer, Patente> obterPatente = indice -> Arrays.stream(Patente.values())
                            .filter(p -> p.getNome().equals(servico.get(indice).patente()))
                            .findFirst().orElseThrow();
                    var militarEscalado = obterPatente.apply(0).name() + " "
                            + servico.get(0).nomePaz();
                    celula.incluirNaCelula(celula.criarCelula(indiceColuna), militarEscalado);
                    if (servico.size() > 1) {
                        militarEscalado = obterPatente.apply(1).name() + " "
                                + servico.get(1).nomePaz();
                        celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(indiceColuna), militarEscalado);
                    } else if (funcao.equals(Funcao.OPERADOR_DE_LINHA))
                        celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(indiceColuna), "----------------------");
                } else
                    celula.incluirNaCelula(celula.criarCelula(indiceColuna), "----------------------");
                indiceColuna++;
            }
            if (this.indiceDataServico >= this.datasServicos.size()) {
                var celulasVazias = diasSemana.size() - this.contadorDias;
                for (int contador = 0; contador < celulasVazias; contador++) {
                    celula.incluirNaCelula(celula.criarCelula(indiceColuna), "----------------------");
                    if (funcao.equals(Funcao.OPERADOR_DE_LINHA))
                        celulaAbaixo.incluirNaCelula(celulaAbaixo.criarCelula(indiceColuna), "----------------------");
                    indiceColuna++;
                }
            }
        }
    }

}
