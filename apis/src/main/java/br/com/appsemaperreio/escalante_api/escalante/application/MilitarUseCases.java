package br.com.appsemaperreio.escalante_api.escalante.application;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.dto.MilitarEscalavel;

public interface MilitarUseCases {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

}
