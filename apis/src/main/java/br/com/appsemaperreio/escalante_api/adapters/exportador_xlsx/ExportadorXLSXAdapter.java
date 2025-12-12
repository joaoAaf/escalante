package br.com.appsemaperreio.escalante_api.adapters.exportador_xlsx;

import java.io.OutputStream;
import java.util.List;

import br.com.appsemaperreio.escalante_api.dto.escalante.ServicoOperacionalDto;

public interface ExportadorXLSXAdapter {

    void exportarEscalaXLSX(OutputStream outputStream, List<ServicoOperacionalDto> servicos) throws Exception;

}
