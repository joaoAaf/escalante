package br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.apachepoi;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

public abstract class ImportadorBaseXLSX {

    protected String validarCelulas(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Uma célula obrigatória está vazia. Verifique se todas as colunas estão preenchidas.");
        }
        return switch (cell.getCellType()) {
            case BLANK -> throw new IllegalArgumentException(
                    String.format("A célula [%d, %d] está vazia", cell.getRowIndex() + 1, cell.getColumnIndex() + 1));
            case ERROR -> throw new IllegalArgumentException(
                    String.format("A célula [%d, %d] contém um erro", cell.getRowIndex() + 1, cell.getColumnIndex() + 1));
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    yield date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
                }
                yield BigDecimal.valueOf(cell.getNumericCellValue()).toBigInteger().toString();
            }
            default -> throw new IllegalArgumentException(
                    String.format("A célula [%d, %d] não é do tipo esperado", cell.getRowIndex() + 1,
                            cell.getColumnIndex() + 1));
        };
    }

}
