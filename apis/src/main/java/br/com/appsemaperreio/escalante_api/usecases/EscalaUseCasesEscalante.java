package br.com.appsemaperreio.escalante_api.usecases;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.dtos.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.dtos.ServicoOperacionalDto;

public interface EscalaUseCasesEscalante {

    byte[] obterPlanilhaModeloEscala();
    
    List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilhaEscala);
    
    List<ServicoOperacionalDto> criarEscalaAutomatica(DadosEscalaRequest request);
    
    byte[] exportarEscalaXLSX(List<ServicoOperacionalDto> servicos) throws Exception;

}
