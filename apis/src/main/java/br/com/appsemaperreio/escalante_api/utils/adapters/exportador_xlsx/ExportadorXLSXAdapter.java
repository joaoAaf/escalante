package br.com.appsemaperreio.escalante_api.utils.adapters.exportador_xlsx;

import java.io.OutputStream;
import java.util.List;

import br.com.appsemaperreio.escalante_api.dtos.escalante.ServicoOperacionalDto;

public interface ExportadorXLSXAdapter {

    void exportarEscalaXLSX(OutputStream outputStream, List<ServicoOperacionalDto> servicos) throws Exception;

}
