package br.com.appsemaperreio.escalante_api.escalante.model.application.service;

import br.com.appsemaperreio.escalante_api.escalante.adapters.exportador_xlsx.ExportadorXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorEscalaXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.model.application.IEscalaService;
import br.com.appsemaperreio.escalante_api.escalante.model.application.mappers.MilitarMapper;
import br.com.appsemaperreio.escalante_api.escalante.model.application.mappers.ServicoOperacionalMapper;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.DadosEscala;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions.ErroLeituraPlanilhaModeloException;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions.PlanilhaModeloNaoEncontradaException;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.factories.EscalaFactory;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.factories.ServicoOperacionalFactory;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoOperacionalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EscalaService extends MetodosCompartilhados implements IEscalaService {

    @Autowired
    private ImportadorEscalaXLSXAdapter importadorXLSX;

    @Autowired
    private ExportadorXLSXAdapter exportadorXLSX;

    @Autowired
    private MilitarMapper militarMapper;

    @Autowired
    private ServicoOperacionalMapper servicoOperacionalMapper;

    @Override
    public byte[] obterPlanilhaModeloEscala() {
        try (var inputStream = getClass().getResourceAsStream("/samples/modelo_importacao_escala.xlsx")) {
            if (inputStream == null)
                throw new PlanilhaModeloNaoEncontradaException("Não foi possível encontrar a planilha modelo.");
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new ErroLeituraPlanilhaModeloException("Erro ao ler a planilha modelo de escala.", e);
        }
    }

    @Override
    public List<ServicoOperacionalDto> importarEscalaXLSX(MultipartFile planilhaEscala) {
        validarPlanilha(planilhaEscala);
        return importadorXLSX.importarEscalaXLSX(planilhaEscala);
    }

    @Override
    public List<ServicoOperacionalDto> criarEscalaAutomatica(DadosEscalaRequest request) {
        request.validarDatas();

        var militares = militarMapper.toListMilitar(request.militares());
        var militaresPorMatricula = militares.stream().collect(Collectors.toMap(Militar::getMatricula, m -> m));

        var servicosAnteriores = Optional.ofNullable(request.servicosAnteriores()).orElse(List.of())
                .stream()
                .map(servicoDto -> {
                    var militar = militaresPorMatricula.get(servicoDto.matricula());
                    if (militar == null) return null;
                    var servico = ServicoOperacionalFactory.criarServicoOperacional(
                            Funcao.obterFuncaoPorNome(servicoDto.funcao()),
                            servicoDto.dataServico()
                    );
                    servico.setMilitar(militar);
                    return servico;
                })
                .filter(Objects::nonNull)
                .toList();

        if (!servicosAnteriores.isEmpty())
            militares.forEach(militar -> militar.ultimosServicosConsecutivos(servicosAnteriores));

        var dadosEscala = new DadosEscala(request.dataInicio(), request.dataFim(), request.diasServico());
        var escala = EscalaFactory.criarEscala(dadosEscala);
        escala.preencherEscala(militares);

        return servicoOperacionalMapper.toListServicoOperacionalDto(escala.getServicosEscala());
    }

    @Override
    public byte[] exportarEscalaXLSX(List<ServicoOperacionalDto> servicos) throws Exception {
        try (var outputStream = new ByteArrayOutputStream()) {
            exportadorXLSX.exportarEscalaXLSX(outputStream, servicos);
            return outputStream.toByteArray();
        }
    }

}
