package apisemaperreio.escalante.escalante.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import apisemaperreio.escalante.escalante.dtos.DadosEscalaRequest;
import apisemaperreio.escalante.escalante.dtos.ServicoOperacionalDto;
import apisemaperreio.escalante.escalante.usecases.EscalaUseCasesEscalante;
import apisemaperreio.escalante.escalante.utils.adapters.exportador_xlsx.ExportadorXLSXAdapter;
import apisemaperreio.escalante.escalante.utils.adapters.importador_xlsx.ImportadorEscalaXLSXAdapter;
import apisemaperreio.escalante.escalante.utils.factories.EscalaFactory;
import apisemaperreio.escalante.escalante.utils.factories.ServicoOperacionalFactory;
import apisemaperreio.escalante.escalante.utils.mappers.MilitarMapperEscalante;
import apisemaperreio.escalante.escalante.utils.mappers.ServicoOperacionalMapper;
import apisemaperreio.escalante.escalante.domain.exceptions.PlanilhaModeloNaoEncontradaException;
import apisemaperreio.escalante.escalante.domain.escalante.DadosEscala;
import apisemaperreio.escalante.escalante.domain.escalante.Funcao;
import apisemaperreio.escalante.escalante.domain.escalante.ServicoOperacional;
import apisemaperreio.escalante.escalante.domain.exceptions.ErroLeituraPlanilhaModeloException;

@Service
public class EscalaServiceEscalante extends BaseServiceEscalante implements EscalaUseCasesEscalante {

    @Autowired
    private ImportadorEscalaXLSXAdapter importadorXLSX;

    @Autowired
    private ExportadorXLSXAdapter exportadorXLSX;

    @Autowired
    private MilitarMapperEscalante militarMapper;

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
        var servicosAnteriores = new ArrayList<ServicoOperacional>();
        if (request.servicosAnteriores() != null && !request.servicosAnteriores().isEmpty()) {
            request.servicosAnteriores().forEach(servicoDto -> {
                var servico = ServicoOperacionalFactory.criarServicoOperacional(Funcao.obterFuncaoPorNome(servicoDto.funcao()), servicoDto.dataServico());
                var militar = militares.stream()
                        .filter(m -> m.getMatricula().equals(servicoDto.matricula()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Militar não encontrado: " + servicoDto.matricula()));
                servico.setMilitar(militar);
                servicosAnteriores.add(servico);
            });
            militares.forEach(militar -> militar.ultimosServicosConsecutivos(servicosAnteriores));
        }
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
