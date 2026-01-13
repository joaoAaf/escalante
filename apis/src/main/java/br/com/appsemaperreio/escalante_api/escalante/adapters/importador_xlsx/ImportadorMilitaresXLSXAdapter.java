package br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx;

import java.util.List;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImportadorMilitaresXLSXAdapter {

    List<MilitarDto> importarMilitaresXLSX(MultipartFile planilha);

}
