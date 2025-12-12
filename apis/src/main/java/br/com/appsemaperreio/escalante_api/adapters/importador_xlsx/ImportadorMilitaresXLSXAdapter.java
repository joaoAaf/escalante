package br.com.appsemaperreio.escalante_api.adapters.importador_xlsx;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dto.escalante.MilitarEscalavel;

public interface ImportadorMilitaresXLSXAdapter {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilha);

}
