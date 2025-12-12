package br.com.appsemaperreio.escalante_api.adapters.importador_xlsx.apachepoi;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.adapters.importador_xlsx.ImportadorEscalaXLSXAdapter;
import br.com.appsemaperreio.escalante_api.domain.escalante.exceptions.ErroProcessamentoPlanilhaException;
import br.com.appsemaperreio.escalante_api.dto.escalante.ServicoOperacionalDto;

@Component
public class ImportadorEscalaXLSXApachePoi extends ImportadorBaseXLSX implements ImportadorEscalaXLSXAdapter {

    @Override
    public List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilha) {

        List<ServicoOperacionalDto> escala = new ArrayList<>();

        try {
            var workbook = WorkbookFactory.create(planilha.getInputStream());
            var sheet = workbook.getSheetAt(1);

            if (sheet.getLastRowNum() > 210)
                throw new IllegalArgumentException("A planilha excede o limite máximo de 210 serviços operacionais.");

            var rowIterator = sheet.iterator();
            rowIterator.next();

            if (!rowIterator.hasNext())
                throw new IllegalArgumentException("A planilha está vazia.");

            while (rowIterator.hasNext()) {
                var row = rowIterator.next();

                try {
                    var dataServico = LocalDate.parse(validarCelulas(row.getCell(0)));
                    var matricula = validarCelulas(row.getCell(1));
                    var nomePaz = validarCelulas(row.getCell(2));
                    var patente = validarCelulas(row.getCell(3));
                    var antiguidade = Integer.valueOf(validarCelulas(row.getCell(4)));
                    var funcao = validarCelulas(row.getCell(5));
                    var folga = Integer.valueOf(validarCelulas(row.getCell(6)));

                    var servico = new ServicoOperacionalDto(
                            dataServico,
                            matricula,
                            nomePaz,
                            patente,
                            antiguidade,
                            funcao,
                            folga);

                    escala.add(servico);

                } catch (NumberFormatException | DateTimeParseException e) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Erro de formato de dados na linha %d. Verifique se os números ou datas estão corretos.",
                                    row.getRowNum() + 1),
                            e);
                }
            }

        } catch (IllegalArgumentException e) {
            escala.clear();
            throw e;
        } catch (Exception e) {
            escala.clear();
            throw new ErroProcessamentoPlanilhaException("Erro ao processar a planilha de serviços operacionais.", e);
        }

        return escala;

    }

}