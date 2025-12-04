package apisemaperreio.escalante.escalante.usecases;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import apisemaperreio.escalante.escalante.dtos.MilitarEscalavel;

public interface MilitarUseCasesEscalante {

    List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

}
