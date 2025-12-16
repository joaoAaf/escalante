package br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.apachepoi;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.domain.exceptions.ErroProcessamentoPlanilhaException;
import br.com.appsemaperreio.escalante_api.escalante.dto.MilitarEscalavel;

@Component
public class ImportadorMilitaresXLSXApachePoi extends ImportadorBaseXLSX implements ImportadorMilitaresXLSXAdapter {

    @Override
    public List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilha) {

        List<MilitarEscalavel> militares = new ArrayList<>();

        try {
            var workbook = WorkbookFactory.create(planilha.getInputStream());
            var sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() > 100)
                throw new IllegalArgumentException("A planilha excede o limite máximo de 100 militares.");

            var rowIterator = sheet.iterator();
            rowIterator.next();

            if (!rowIterator.hasNext())
                throw new IllegalArgumentException("A planilha está vazia.");

            while (rowIterator.hasNext()) {
                var row = rowIterator.next();

                try {
                    var antiguidade = Integer.valueOf(validarCelulas(row.getCell(0)));
                    var matricula = validarCelulas(row.getCell(1));
                    var patente = validarCelulas(row.getCell(2));
                    var nomePaz = validarCelulas(row.getCell(3));
                    var nascimento = LocalDate.parse(validarCelulas(row.getCell(4)));
                    var folgaEspecial = Integer.valueOf(validarCelulas(row.getCell(5)));
                    var cov = validarCelulas(row.getCell(6)).equalsIgnoreCase("sim");

                    var militar = new MilitarEscalavel(
                            antiguidade,
                            matricula,
                            patente,
                            nomePaz,
                            nascimento,
                            folgaEspecial,
                            cov);

                    militares.add(militar);

                } catch (NumberFormatException | DateTimeParseException e) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Erro de formato de dados na linha %d. Verifique se os números ou datas estão corretos.",
                                    row.getRowNum() + 1),
                            e);
                }
            }

        } catch (IllegalArgumentException e) {
            militares.clear();
            throw e;
        } catch (Exception e) {
            militares.clear();
            throw new ErroProcessamentoPlanilhaException("Erro ao processar a planilha de militares.", e);
        }

        return militares;

    }

}