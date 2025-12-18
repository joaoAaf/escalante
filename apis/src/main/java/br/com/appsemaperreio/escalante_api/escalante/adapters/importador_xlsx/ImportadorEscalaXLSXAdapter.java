package br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;

public interface ImportadorEscalaXLSXAdapter {

    List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilha);

}
