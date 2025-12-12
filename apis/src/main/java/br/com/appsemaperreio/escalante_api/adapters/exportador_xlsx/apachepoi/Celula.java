package br.com.appsemaperreio.escalante_api.adapters.exportador_xlsx.apachepoi;

import java.time.LocalDate;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class Celula {

    protected XSSFRow linha;
    protected XSSFCellStyle estilo;

    public Celula(XSSFRow linha, XSSFCellStyle estilo) {
        this.linha = linha;
        this.estilo = estilo;
    }

    public XSSFCell criarCelula(int indiceColuna) {
        var celula = this.linha.createCell(indiceColuna);
        celula.setCellStyle(this.estilo);
        return celula;
    }

    public void incluirNaCelula(XSSFCell celula, Object valor) {
        if (valor == null) {
            celula.setCellValue("");
        } else if (valor instanceof String) {
            celula.setCellValue((String) valor);
        } else if (valor instanceof Integer) {
            celula.setCellValue((Integer) valor);
        } else if (valor instanceof LocalDate) {
            this.formatarData(celula);
            celula.setCellValue((LocalDate) valor);
        } else {
            celula.setCellValue("");
        }
    }

    private void formatarData(XSSFCell celula) {
        var formatoData = this.estilo.copy();
        formatoData.setDataFormat(celula.getRow().getSheet().getWorkbook().createDataFormat().getFormat("dd/MM/yyyy"));
        celula.setCellStyle(formatoData);
    }

}
