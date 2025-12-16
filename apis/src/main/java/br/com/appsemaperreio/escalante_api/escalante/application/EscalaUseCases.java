package br.com.appsemaperreio.escalante_api.escalante.application;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.dto.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.escalante.dto.ServicoOperacionalDto;

public interface EscalaUseCases {

    byte[] obterPlanilhaModeloEscala();
    
    List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilhaEscala);
    
    List<ServicoOperacionalDto> criarEscalaAutomatica(DadosEscalaRequest request);
    
    byte[] exportarEscalaXLSX(List<ServicoOperacionalDto> servicos) throws Exception;

}
