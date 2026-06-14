package br.com.appsemaperreio.escalante_api.escalante.unitarios;

import br.com.appsemaperreio.escalante_api.escalante.model.application.mappers.MilitarMapper;
import br.com.appsemaperreio.escalante_api.escalante.model.application.mappers.ServicoOperacionalMapper;
import br.com.appsemaperreio.escalante_api.escalante.model.application.service.EscalaService;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.ServicoOperacional;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.DadosEscalaRequest;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.MilitarDto;
import br.com.appsemaperreio.escalante_api.escalante.model.dto.ServicoAnteriorDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class EscalaServiceTest {

    @Mock
    private MilitarMapper militarMapper;

    @Mock
    private ServicoOperacionalMapper servicoOperacionalMapper;

    @InjectMocks
    private EscalaService service;

    @BeforeEach
    void limparEstadoEstatico() throws Exception {
        Field campo = ServicoOperacional.class.getDeclaredField("covFiscal");
        campo.setAccessible(true);
        campo.set(null, Optional.empty());
        lenient().when(servicoOperacionalMapper.toListServicoOperacionalDto(any()))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void quandoServicosAnterioresSaoNulos_naoChamaUltimosServicosConsecutivos() {
        var militares = spyAll(MockDadosEscala.criarMilitares());
        when(militarMapper.toListMilitar(any())).thenReturn(militares);

        service.criarEscalaAutomatica(requestValido(militares, null));

        for (var m : militares)
            verify(m, never()).ultimosServicosConsecutivos(any());
    }

    @Test
    void quandoServicosAnterioresVazios_naoChamaUltimosServicosConsecutivos() {
        var militares = spyAll(MockDadosEscala.criarMilitares());
        when(militarMapper.toListMilitar(any())).thenReturn(militares);

        service.criarEscalaAutomatica(requestValido(militares, List.of()));

        for (var m : militares)
            verify(m, never()).ultimosServicosConsecutivos(any());
    }

    @Test
    void quandoServicoAnteriorPossuiMatriculaInexistente_eDescartado() {
        var militares = spyAll(MockDadosEscala.criarMilitares());
        when(militarMapper.toListMilitar(any())).thenReturn(militares);

        var request = requestValido(militares, List.of(
                criarServicoDto("99999999", Funcao.COV, LocalDate.of(2026, 5, 30))
        ));
        service.criarEscalaAutomatica(request);

        for (var m : militares)
            verify(m, never()).ultimosServicosConsecutivos(any());
    }

    @Test
    void quandoServicoAnteriorPossuiMatriculaValida_chamaMetodoComServicoAtribuidoAoMilitarCorreto() {
        var militares = spyAll(MockDadosEscala.criarMilitares());
        when(militarMapper.toListMilitar(any())).thenReturn(militares);

        var militarAlvo = localizar(militares, "89010001");
        var dataServico = LocalDate.of(2026, 5, 30);
        var funcao = Funcao.FISCAL_DE_DIA;

        var request = requestValido(militares, List.of(
                criarServicoDto("89010001", funcao, dataServico)
        ));
        service.criarEscalaAutomatica(request);

        var captor = ArgumentCaptor.forClass(List.class);
        verify(militarAlvo).ultimosServicosConsecutivos(captor.capture());

        var lista = (List<ServicoOperacional>) captor.getValue();
        assertEquals(1, lista.size());
        var servico = lista.getFirst();
        assertEquals(militarAlvo, servico.getMilitar());
        assertEquals(dataServico, servico.getDataServico());
        assertEquals(funcao, servico.getFuncao());
    }

    @Test
    void misturaMatriculasValidasEInvalidas_listaPassadaContemSomenteAsValidas() {
        var militares = spyAll(MockDadosEscala.criarMilitares());
        when(militarMapper.toListMilitar(any())).thenReturn(militares);

        var m1 = localizar(militares, "89010001");
        var m2 = localizar(militares, "89010002");
        var data = LocalDate.of(2026, 5, 30);

        var request = requestValido(militares, List.of(
                criarServicoDto("89010001", Funcao.FISCAL_DE_DIA, data),
                criarServicoDto("99999999", Funcao.COV, data),
                criarServicoDto("89010002", Funcao.COV, data)
        ));
        service.criarEscalaAutomatica(request);

        var captorM1 = ArgumentCaptor.forClass(List.class);
        verify(m1).ultimosServicosConsecutivos(captorM1.capture());
        var captorM2 = ArgumentCaptor.forClass(List.class);
        verify(m2).ultimosServicosConsecutivos(captorM2.capture());

        var listaM1 = (List<ServicoOperacional>) captorM1.getValue();
        var listaM2 = (List<ServicoOperacional>) captorM2.getValue();
        assertEquals(2, listaM1.size());
        assertEquals(2, listaM2.size());
        assertEquals(m1, listaM1.get(0).getMilitar());
        assertEquals(m2, listaM1.get(1).getMilitar());
    }

    @Test
    void funcaoDoServicoAnteriorEMapeadaPeloNome() {
        var militares = spyAll(MockDadosEscala.criarMilitares());
        when(militarMapper.toListMilitar(any())).thenReturn(militares);

        var militarAlvo = localizar(militares, "89010003");

        var request = requestValido(militares, List.of(
                criarServicoDto("89010003", Funcao.OPERADOR_DE_LINHA, LocalDate.of(2026, 5, 29))
        ));
        service.criarEscalaAutomatica(request);

        var captor = ArgumentCaptor.forClass(List.class);
        verify(militarAlvo).ultimosServicosConsecutivos(captor.capture());
        var servico = ((List<ServicoOperacional>) captor.getValue()).getFirst();
        assertEquals(Funcao.OPERADOR_DE_LINHA, servico.getFuncao());
    }

    private List<Militar> spyAll(List<Militar> militares) {
        return militares.stream().map(m -> (Militar) spy(m)).toList();
    }

    private Militar localizar(List<Militar> militares, String matricula) {
        return militares.stream()
                .filter(m -> m.getMatricula().equals(matricula))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Militar " + matricula + " ausente na fixture"));
    }

    private DadosEscalaRequest requestValido(List<Militar> militares, List<ServicoAnteriorDto> servicosAnteriores) {
        return new DadosEscalaRequest(
                LocalDate.of(2026, 6, 2),
                LocalDate.of(2026, 6, 5),
                2,
                militares.stream().map(this::toDto).toList(),
                servicosAnteriores
        );
    }

    private MilitarDto toDto(Militar m) {
        return new MilitarDto(
                m.getAntiguidade(),
                m.getMatricula(),
                m.getPatente().getNome(),
                m.getNomePaz(),
                m.getNascimento(),
                m.getFolgaEspecial(),
                m.getCov()
        );
    }

    private ServicoAnteriorDto criarServicoDto(String matricula, Funcao funcao, LocalDate data) {
        return new ServicoAnteriorDto(
                data,
                matricula,
                funcao.getNome()
        );
    }
}
