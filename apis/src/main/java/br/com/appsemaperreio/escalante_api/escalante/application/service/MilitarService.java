package br.com.appsemaperreio.escalante_api.escalante.application.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.application.MilitarUseCases;
import br.com.appsemaperreio.escalante_api.escalante.domain.exceptions.ErroLeituraPlanilhaModeloException;
import br.com.appsemaperreio.escalante_api.escalante.domain.exceptions.PlanilhaModeloNaoEncontradaException;
import br.com.appsemaperreio.escalante_api.escalante.dto.MilitarEscalavel;

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
