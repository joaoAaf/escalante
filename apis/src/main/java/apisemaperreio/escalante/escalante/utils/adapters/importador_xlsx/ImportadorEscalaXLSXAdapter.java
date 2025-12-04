package apisemaperreio.escalante.escalante.utils.adapters.importador_xlsx;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import apisemaperreio.escalante.escalante.dtos.ServicoOperacionalDto;

public interface ImportadorEscalaXLSXAdapter {

    List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilha);

}
