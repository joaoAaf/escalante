package br.com.appsemaperreio.escalante_api.application.escalante;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dto.escalante.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.dto.escalante.ServicoOperacionalDto;

public interface EscalaUseCases {

    byte[] obterPlanilhaModeloEscala();
    
    List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilhaEscala);
    
    List<ServicoOperacionalDto> criarEscalaAutomatica(DadosEscalaRequest request);
    
    byte[] exportarEscalaXLSX(List<ServicoOperacionalDto> servicos) throws Exception;

}
