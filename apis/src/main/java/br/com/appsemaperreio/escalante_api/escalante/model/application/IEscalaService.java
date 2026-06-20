package br.com.appsemaperreio.escalante_api.escalante.model.application;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.model.dto.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoAnteriorDto;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;

public interface IEscalaService {

    byte[] obterPlanilhaModeloEscala();
    
    List<ServicoAnteriorDto> importarEscalaXLSX(MultipartFile planilhaEscala);
    
    List<ServicoOperacionalDto> criarEscalaAutomatica(DadosEscalaRequest request);
    
    byte[] exportarEscalaXLSX(List<ServicoOperacionalDto> servicos) throws Exception;

}