package apisemaperreio.escalante.escalante.usecases;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import apisemaperreio.escalante.escalante.dtos.DadosEscalaRequest;
import apisemaperreio.escalante.escalante.dtos.ServicoOperacionalDto;

public interface EscalaUseCasesEscalante {

    byte[] obterPlanilhaModeloEscala();
    
    List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilhaEscala);
    
    List<ServicoOperacionalDto> criarEscalaAutomatica(DadosEscalaRequest request);
    
    byte[] exportarEscalaXLSX(List<ServicoOperacionalDto> servicos) throws Exception;

}
