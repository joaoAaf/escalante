package br.com.appsemaperreio.escalante_api.escalante.model.application;

import java.util.List;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;

public interface IMilitarService {

    List<MilitarDto> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

    List<MilitarDto> cadastrarMilitares(List<MilitarDto> militares);

    List<MilitarDto> listarMilitares();

    MilitarDto atualizarMilitar(MilitarDto militarDto);

    void deletarMilitar(String matricula);

}
