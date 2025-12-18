package br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx;

import java.io.OutputStream;
import java.util.List;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;

public interface ExportadorXLSXAdapter {

    void exportarEscalaXLSX(OutputStream outputStream, List<ServicoOperacionalDto> servicos) throws Exception;

}
