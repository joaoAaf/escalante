package br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx.apachepoi;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.appsemaperreio.escalante_api.escalante.dto.ServicoOperacionalDto;

public class AbaConferencia {

    private XSSFSheet planilha;
    private int numeroLinha;
    private XSSFRow linha;

    public AbaConferencia(XSSFWorkbook workbook) {
        this.planilha = workbook.createSheet("Conferência");
        this.numeroLinha = 0;
        this.linha = planilha.createRow(this.numeroLinha++);
    }

    public void criarAbaConferencia(XSSFWorkbook workbook, List<ServicoOperacionalDto> servicos) {
        var estilos = new EstilosPlanilha(workbook);

        this.criarCabecalhoAbaConferencia(estilos.getEstiloCabecalho1());

        for (var servico : servicos) {
            this.linha = this.planilha.createRow(this.numeroLinha++);
            this.criarLinhasAbaConferencia(estilos.getEstiloPadrao(), servico);
        }
    }

    private void criarCabecalhoAbaConferencia(XSSFCellStyle estilo) {
        int indiceColuna = 0;
        var celula = new Celula(this.linha, estilo);
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Data");
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Matricula");
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Militar Escalado");
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Posto/ Graduação");
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Antiguidade");
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Função");
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), "Folga");
    }

    private void criarLinhasAbaConferencia(XSSFCellStyle estilo, ServicoOperacionalDto servico) {
        int indiceColuna = 0;
        var celula = new Celula(this.linha, estilo);
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), servico.dataServico());
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), servico.matricula());
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), servico.nomePaz());
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), servico.patente());
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), servico.antiguidade());
        celula.incluirNaCelula(celula.criarCelula(indiceColuna++), servico.funcao());
        celula.incluirNaCelula(celula.criarCelula(indiceColuna), servico.folga());
    }

}
