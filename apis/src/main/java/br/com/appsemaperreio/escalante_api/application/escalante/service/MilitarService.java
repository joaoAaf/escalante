package br.com.appsemaperreio.escalante_api.application.escalante.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import br.com.appsemaperreio.escalante_api.application.escalante.MilitarUseCases;
import br.com.appsemaperreio.escalante_api.domain.escalante.exceptions.ErroLeituraPlanilhaModeloException;
import br.com.appsemaperreio.escalante_api.domain.escalante.exceptions.PlanilhaModeloNaoEncontradaException;
import br.com.appsemaperreio.escalante_api.dto.escalante.MilitarEscalavel;

@Service
public class MilitarService extends BaseService implements MilitarUseCases {

    @Autowired
    private ImportadorMilitaresXLSXAdapter importadorMilitaresXLSXAdapter;

    @Override
    public List<MilitarEscalavel> importarMilitaresXLSX(MultipartFile planilhaMilitares) {
        validarPlanilha(planilhaMilitares);
        return importadorMilitaresXLSXAdapter.importarMilitaresXLSX(planilhaMilitares);
    }

    @Override
    public byte[] obterPlanilhaModeloMilitares() {
        try (var inputStream = getClass().getResourceAsStream("/samples/modelo_importacao_militares.xlsx")) {
            if (inputStream == null)
                throw new PlanilhaModeloNaoEncontradaException("Não foi possível encontrar a planilha modelo.");
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new ErroLeituraPlanilhaModeloException("Erro ao ler a planilha modelo de militares.", e);
        }
    }

}
