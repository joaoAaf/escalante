package br.com.appsemaperreio.escalante_api.escalante.unitarios;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.DadosEscala;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Escala;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.ServicoOperacional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EscalaTest {

    @BeforeEach
    void limparEstadoEstatico() throws Exception {
        Field campo = ServicoOperacional.class.getDeclaredField("covFiscal");
        campo.setAccessible(true);
        campo.set(null, Optional.empty());
    }

    @Test
    void deveEscalarTodosOsDiasComPeloMenos5Militares() {
        var escala = criarEscala();
        var servicosPorDia = servicosPorDia(escala);

        for (var entry : servicosPorDia.entrySet()) {
            var data = entry.getKey();
            var servicos = entry.getValue();
            assertTrue(servicos.size() >= 5,
                    "Dia " + data + " possui apenas " + servicos.size() + " serviços escalados");
        }
    }

    @Test
    void deveHaver154ServicosNaEscala() {
        var escala = criarEscala();
        var totalServicos = escala.getServicosEscala().size();
        assertEquals(154, totalServicos, "Esperava-se exatamente 154 serviços na escala, mas foram encontrados: " + totalServicos);
    }

    private Escala criarEscala() {
        var militares = MockDadosEscala.criarMilitares();
        var servicosAnteriores = MockDadosEscala.criarServicosAnteriores(militares);
        militares.forEach(m -> m.ultimosServicosConsecutivos(servicosAnteriores));

        var dadosEscala = new DadosEscala(
                MockDadosEscala.DATA_INICIAL,
                MockDadosEscala.DATA_FINAL,
                MockDadosEscala.DIAS_SERVICO);
        var escala = new Escala(dadosEscala);
        escala.preencherEscala(militares);

        return escala;
    }

    private Map<LocalDate, List<ServicoOperacional>> servicosPorDia(Escala escala) {
        return escala.getServicosEscala().stream()
                .collect(java.util.stream.Collectors.groupingBy(ServicoOperacional::getDataServico));
    }


}
