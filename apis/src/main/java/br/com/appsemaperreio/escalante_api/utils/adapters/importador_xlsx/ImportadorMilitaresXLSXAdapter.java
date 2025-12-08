package br.com.appsemaperreio.escalante_api.utils.adapters.importador_xlsx;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dtos.MilitarEscalavel;

public interface ImportadorMilitaresXLSXAdapter {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilha);

}
