package br.com.appsemaperreio.escalante_api.domain.escalante.factories;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import br.com.appsemaperreio.escalante_api.domain.escalante.AjudanteLinha;
import br.com.appsemaperreio.escalante_api.domain.escalante.Cov;
import br.com.appsemaperreio.escalante_api.domain.escalante.FiscalDia;
import br.com.appsemaperreio.escalante_api.domain.escalante.Funcao;
import br.com.appsemaperreio.escalante_api.domain.escalante.OperadorLinha;
import br.com.appsemaperreio.escalante_api.domain.escalante.Permanente;
import br.com.appsemaperreio.escalante_api.domain.escalante.ServicoOperacional;

public final class ServicoOperacionalFactory {

    private static final Map<Funcao, Function<LocalDate, ServicoOperacional>> FUNCOES_MAP = new EnumMap<>(Funcao.class);

    static {
        FUNCOES_MAP.put(Funcao.COV, Cov::new);
        FUNCOES_MAP.put(Funcao.PERMANENTE, Permanente::new);
        FUNCOES_MAP.put(Funcao.AJUDANTE_DE_LINHA, AjudanteLinha::new);
        FUNCOES_MAP.put(Funcao.OPERADOR_DE_LINHA, OperadorLinha::new);
        FUNCOES_MAP.put(Funcao.FISCAL_DE_DIA, FiscalDia::new);
    }

    private ServicoOperacionalFactory() {}

    public static ServicoOperacional criarServicoOperacional(Funcao funcao, LocalDate dataServico) {
        var servicoOperacional = FUNCOES_MAP.get(funcao);
        if (servicoOperacional == null) {
            throw new IllegalArgumentException("A Função atribuída ao Serviço Operacional é inválida.");
        }
        return servicoOperacional.apply(dataServico);
    }
}