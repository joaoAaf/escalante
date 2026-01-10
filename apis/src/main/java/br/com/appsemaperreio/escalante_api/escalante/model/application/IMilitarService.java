package br.com.appsemaperreio.escalante_api.escalante.model.application;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IMilitarService {

    List<MilitarDto> importarMilitaresXLSX(MultipartFile planilhaMilitares);

    byte[] obterPlanilhaModeloMilitares();

    void cadastrarMilitares(List<MilitarDto> militares);

    MilitarDto obterMilitarPorMatricula(String matricula);

    List<MilitarDto> listarMilitares();

    void atualizarMilitar(MilitarDto militarDto);

    void deletarMilitar(String matricula);

}
