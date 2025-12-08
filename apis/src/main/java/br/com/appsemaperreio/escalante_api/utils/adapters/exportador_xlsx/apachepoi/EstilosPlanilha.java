package br.com.appsemaperreio.escalante_api.utils.adapters.exportador_xlsx.apachepoi;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EstilosPlanilha {

    private XSSFCellStyle estiloPadrao;
    private XSSFCellStyle estiloCabecalho1;
    private XSSFCellStyle estiloCabecalho2;

    public EstilosPlanilha(XSSFWorkbook workbook) {
        this.estiloPadrao = definirEstiloPadrao(workbook);
        this.estiloCabecalho1 = definirEstiloCabecalho1(workbook);
        this.estiloCabecalho2 = definirEstiloCabecalho2(workbook);
    }

    private XSSFCellStyle definirEstiloCabecalho2(XSSFWorkbook workbook) {
        var estilo = definirEstiloCabecalho1(workbook);
        estilo.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        return estilo;
    }

    private XSSFCellStyle definirEstiloCabecalho1(XSSFWorkbook workbook) {
        var estilo = definirEstiloPadrao(workbook);
        var fonte = definirFonte(workbook, true);
        estilo.setFont(fonte);
        estilo.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return estilo;
    }

    private XSSFCellStyle definirEstiloPadrao(XSSFWorkbook workbook) {
        var estilo = workbook.createCellStyle();
        var fonte = definirFonte(workbook, false);
        estilo.setFont(fonte);
        estilo.setAlignment(HorizontalAlignment.CENTER);
        estilo.setVerticalAlignment(VerticalAlignment.CENTER);
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);
        return estilo;
    }

    private Font definirFonte(XSSFWorkbook workbook, boolean negrito) {
        var fonte = workbook.createFont();
        fonte.setBold(negrito);
        fonte.setFontHeight(12);
        fonte.setFontName("Arial");
        fonte.setColor(IndexedColors.BLACK.getIndex());
        return fonte;
    }

    public XSSFCellStyle getEstiloPadrao() {
        return estiloPadrao;
    }

    public XSSFCellStyle getEstiloCabecalho1() {
        return estiloCabecalho1;
    }

    public XSSFCellStyle getEstiloCabecalho2() {
        return estiloCabecalho2;
    }

}
