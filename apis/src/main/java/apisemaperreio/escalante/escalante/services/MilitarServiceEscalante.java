package apisemaperreio.escalante.escalante.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import apisemaperreio.escalante.escalante.dtos.MilitarEscalavel;
import apisemaperreio.escalante.escalante.usecases.MilitarUseCasesEscalante;
import apisemaperreio.escalante.escalante.utils.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import apisemaperreio.escalante.escalante.domain.exceptions.PlanilhaModeloNaoEncontradaException;
import apisemaperreio.escalante.escalante.domain.exceptions.ErroLeituraPlanilhaModeloException;

@Service
public class MilitarServiceEscalante extends BaseServiceEscalante implements MilitarUseCasesEscalante {

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
