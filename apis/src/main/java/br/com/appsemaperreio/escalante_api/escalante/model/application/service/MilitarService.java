package br.com.appsemaperreio.escalante_api.escalante.model.application.service;

import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.model.application.IMilitarService;
import br.com.appsemaperreio.escalante_api.escalante.model.application.mappers.MilitarMapper;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions.ErroLeituraPlanilhaModeloException;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions.PlanilhaModeloNaoEncontradaException;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import br.com.appsemaperreio.escalante_api.escalante.model.repository.MilitarRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MilitarService extends MetodosCompartilhados implements IMilitarService {

    private final ImportadorMilitaresXLSXAdapter importadorMilitaresXLSXAdapter;
    private final MilitarMapper militarMapper;
    private final MilitarRepository militarRepository;


    public MilitarService(
            ImportadorMilitaresXLSXAdapter importadorMilitaresXLSXAdapter,
            MilitarMapper militarMapper,
            MilitarRepository militarRepository) {
        this.importadorMilitaresXLSXAdapter = importadorMilitaresXLSXAdapter;
        this.militarMapper = militarMapper;
        this.militarRepository = militarRepository;
    }

    @Override
    public List<MilitarDto> importarMilitaresXLSX(MultipartFile planilhaMilitares) {
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

    @Override
    public List<MilitarDto> cadastrarMilitares(List<MilitarDto> militaresDto) {
        var militares = militarMapper.toListMilitar(militaresDto);
        var militaresCadastrados = militarRepository.saveAll(militares);
        return militarMapper.toListMilitarDto(militaresCadastrados);
    }

}
