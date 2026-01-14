package br.com.appsemaperreio.escalante_api.escalante.model.application.service;

import br.com.appsemaperreio.escalante_api.escalante.adapters.importador_xlsx.ImportadorMilitaresXLSXAdapter;
import br.com.appsemaperreio.escalante_api.escalante.model.application.IMilitarService;
import br.com.appsemaperreio.escalante_api.escalante.model.application.mappers.MilitarMapper;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions.ErroLeituraPlanilhaModeloException;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.exceptions.PlanilhaModeloNaoEncontradaException;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import br.com.appsemaperreio.escalante_api.escalante.model.repository.MilitarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private void deslocarAntiguidades(Collection<Integer> antiguidades) {
        if (militarRepository.existsByAntiguidadeIn(antiguidades)) {
            var antiguidadesOrdenadas = antiguidades.stream().sorted().toList();
            for (var antiguidade : antiguidadesOrdenadas) {
                var militaresAntDesc =
                        militarRepository.findByAntiguidadeGreaterThanEqualOrderByAntiguidadeDesc(antiguidade);
                for (Militar m : militaresAntDesc) {
                    m.setAntiguidade(m.getAntiguidade() + 1);
                }
            }
        }
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

    @Transactional
    @Override
    public void cadastrarMilitares(List<MilitarDto> militaresDto) {
        var novosMilitares = militarMapper.toListMilitar(militaresDto);

        var matriculas = novosMilitares.stream()
                .map(Militar::getMatricula)
                .collect(Collectors.toSet());

        if (matriculas.size() < novosMilitares.size()) {
            throw new IllegalArgumentException("Existem matrículas duplicadas na lista de militares a serem cadastrados.");
        }

        var matriculasExistentes = militarRepository.findMatriculasByIdIn(matriculas);

        if (!matriculasExistentes.isEmpty()) {
            var matriculasString = String.join(", ", matriculasExistentes);
            throw new IllegalArgumentException(String.format("As seguintes matrículas já estão cadastradas: %s.", matriculasString));
        }

        var novatos = new ArrayList<Militar>();
        var veteranos = new ArrayList<Militar>();

        for (var militar : novosMilitares) {
            if (militar.getAntiguidade() == 0) {
                novatos.add(militar);
            } else {
                veteranos.add(militar);
            }
        }

        var antiguidades = veteranos.stream()
                .map(Militar::getAntiguidade)
                .collect(Collectors.toSet());

        if (antiguidades.size() < veteranos.size()) {
            throw new IllegalArgumentException("Existem antiguidades duplicadas na lista de militares a serem cadastrados.");
        }

        deslocarAntiguidades(antiguidades);

        militarRepository.flush();

        var prontosParaSalvar = new ArrayList<Militar>(veteranos);

        if (!novatos.isEmpty()) {
            var maiorAntiguidadeBanco = militarRepository.maxAntiguidade();
            var maiorAntiguidadeVeteranos = antiguidades.stream()
                    .max(Comparator.comparingInt(Integer::intValue))
                    .orElse(0);
            var maiorAntiguidade = Math.max(maiorAntiguidadeBanco, maiorAntiguidadeVeteranos);
            for (var novato : novatos) novato.setAntiguidade(++maiorAntiguidade);
            prontosParaSalvar.addAll(novatos);
        }

        militarRepository.saveAll(prontosParaSalvar);
    }

    @Override
    public MilitarDto obterMilitarPorMatricula(String matricula) {
        var militar = militarRepository.findById(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Militar com matrícula "
                        + matricula + " não encontrado."));
        return militarMapper.toMilitarDto(militar);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MilitarDto> listarMilitares() {
        return militarMapper.toListMilitarDto(militarRepository.findAll());
    }

    @Transactional
    @Override
    public void atualizarMilitar(MilitarDto militarDto) {
        var militarExistente = militarRepository.findById(militarDto.matricula())
                .orElseThrow(() -> new IllegalArgumentException("Militar com matrícula "
                        + militarDto.matricula() + " não encontrado."));

        var militarAtualizado = militarMapper.toMilitar(militarDto);

        if (militarExistente.equals(militarAtualizado))
            throw new IllegalArgumentException("Não foram identificadas alterações no militar informado.");

        if (militarAtualizado.getAntiguidade() == 0)
            throw new IllegalArgumentException("A antiguidade do militar não pode ser zero.");

        if (militarExistente.getAntiguidade().equals(militarAtualizado.getAntiguidade())) {
            militarRepository.save(militarAtualizado);
            return;
        }

        if (militarExistente.getAntiguidade() > militarAtualizado.getAntiguidade()) {
            militarExistente.setAntiguidade(militarRepository.maxAntiguidade() + 1);
            militarRepository.flush();
        }

        deslocarAntiguidades(List.of(militarAtualizado.getAntiguidade()));
        militarRepository.flush();

        militarRepository.save(militarAtualizado);
    }

    @Transactional
    @Override
    public void deletarMilitar(String matricula) {
        var militarExistente = militarRepository.findById(matricula)
                .orElseThrow(() -> new IllegalArgumentException("Militar com matrícula "
                        + matricula + " não encontrado."));
        militarRepository.delete(militarExistente);
    }

}
