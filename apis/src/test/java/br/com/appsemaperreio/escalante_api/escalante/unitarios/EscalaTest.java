package br.com.appsemaperreio.escalante_api.escalante.unitarios;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.DadosEscala;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Escala;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.ServicoOperacional;

class EscalaTest {

    @BeforeEach
    void limparEstadoEstatico() throws Exception {
        Field campo = ServicoOperacional.class.getDeclaredField("covFiscal");
        campo.setAccessible(true);
        campo.set(null, java.util.Optional.empty());
    }

    @Test
    void deveEscalarTodosOsDiasComPeloMenos5Militares() {
        var militares = Fixture.criarMilitares();
        var servicosAnteriores = Fixture.criarServicosAnteriores(militares);
        militares.forEach(m -> m.ultimosServicosConsecutivos(servicosAnteriores));

        var dadosEscala = new DadosEscala(
                LocalDate.of(2026, 6, 2),
                LocalDate.of(2026, 6, 30),
                2);
        var escala = new Escala(dadosEscala);
        escala.preencherEscala(militares);

        var servicosPorDia = servicosPorDia(escala);

        for (var entry : servicosPorDia.entrySet()) {
            var data = entry.getKey();
            var servicos = entry.getValue();
            assertTrue(servicos.size() >= 5,
                    "Dia " + data + " possui apenas " + servicos.size() + " serviços escalados");
        }
    }

    @Test
    void quandoCovEhFiscal_deveEscalarDoisOperadoresDeLinha() {
        var militares = Fixture.criarMilitares();
        var servicosAnteriores = Fixture.criarServicosAnteriores(militares);
        militares.forEach(m -> m.ultimosServicosConsecutivos(servicosAnteriores));

        var dadosEscala = new DadosEscala(
                LocalDate.of(2026, 6, 2),
                LocalDate.of(2026, 6, 30),
                2);
        var escala = new Escala(dadosEscala);
        escala.preencherEscala(militares);

        var servicos = escala.getServicosEscala();
        var datasComCovIgualFiscal = datasEmQueCovEhFiscal(servicos);

        assertTrue(!datasComCovIgualFiscal.isEmpty(),
                "Esperava ao menos um dia em que COV seja o mesmo militar do Fiscal de Dia");

        for (var data : datasComCovIgualFiscal) {
            var operadoresNaData = servicos.stream()
                    .filter(s -> s.getDataServico().equals(data)
                            && s.getFuncao() == Funcao.OPERADOR_DE_LINHA)
                    .count();
            assertTrue(operadoresNaData >= 2,
                    "No dia " + data + " (COV=Fiscal) esperava ao menos 2 Operadores de Linha, encontrados: "
                            + operadoresNaData);
        }
    }

    private Map<LocalDate, List<ServicoOperacional>> servicosPorDia(Escala escala) {
        return escala.getServicosEscala().stream()
                .collect(java.util.stream.Collectors.groupingBy(ServicoOperacional::getDataServico));
    }

    private List<LocalDate> datasEmQueCovEhFiscal(List<ServicoOperacional> servicos) {
        var datas = new ArrayList<LocalDate>();
        for (var data : servicos.stream().map(ServicoOperacional::getDataServico).distinct().sorted().toList()) {
            var cov = servicos.stream()
                    .filter(s -> s.getDataServico().equals(data) && s.getFuncao() == Funcao.COV)
                    .findFirst();
            var fiscal = servicos.stream()
                    .filter(s -> s.getDataServico().equals(data) && s.getFuncao() == Funcao.FISCAL_DE_DIA)
                    .findFirst();
            if (cov.isPresent() && fiscal.isPresent()
                    && cov.get().getMilitar().equals(fiscal.get().getMilitar())) {
                datas.add(data);
            }
        }
        return datas;
    }
}
