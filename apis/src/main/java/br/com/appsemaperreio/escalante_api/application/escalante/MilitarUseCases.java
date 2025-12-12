package br.com.appsemaperreio.escalante_api.application.escalante;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dto.escalante.MilitarEscalavel;

public interface MilitarUseCases {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

}
