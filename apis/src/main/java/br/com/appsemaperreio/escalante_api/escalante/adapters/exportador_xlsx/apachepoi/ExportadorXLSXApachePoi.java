package br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx.apachepoi;

import java.io.OutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx.ExportadorXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.dto.ServicoOperacionalDto;

@Component
public class ExportadorXLSXApachePoi implements ExportadorXLSXAdapter {

    @Override
    public void exportarEscalaXLSX(OutputStream outputStream, List<ServicoOperacionalDto> servicos) throws Exception {

        try (var workbook = new XSSFWorkbook()) {
            var abaApresentacao = new AbaApresentacao(workbook, servicos);
            var abaConferencia = new AbaConferencia(workbook);

            abaApresentacao.criarAbaApresentacao(workbook, servicos);
            abaConferencia.criarAbaConferencia(workbook, servicos);
            
            workbook.write(outputStream);
        }
    }

}
