package br.com.appsemaperreio.escalante_api.escalante.model.application;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarEscalavel;

public interface IMilitarService {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

}
