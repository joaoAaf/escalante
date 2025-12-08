package br.com.appsemaperreio.escalante_api.usecases;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dtos.MilitarEscalavel;

public interface MilitarUseCasesEscalante {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

}
