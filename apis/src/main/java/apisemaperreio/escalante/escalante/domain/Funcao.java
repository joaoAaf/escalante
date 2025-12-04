package apisemaperreio.escalante.escalante.domain;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public enum Funcao {

    COV("C.O.V.", 2, Arrays.asList()),
    FISCAL_DE_DIA("Fiscal de Dia", 1, Arrays.asList()),
    PERMANENTE("Permanente", 5, Arrays.asList(Patente.SD, Patente.CB)),
    AJUDANTE_DE_LINHA("Ajudante de Linha", 4, Arrays.asList(Patente.SD, Patente.CB, Patente.SGT)),
    OPERADOR_DE_LINHA("Operador de Linha", 3, Arrays.asList(Patente.CB, Patente.SGT, Patente.SUBTEN, Patente.SD));

    private String nome;
    private int ordemExibicao;
    private List<Patente> patentes;

    private Funcao(String nome, int ordemExibicao, List<Patente> patentes) {
        this.nome = nome;
        this.ordemExibicao = ordemExibicao;
        this.patentes = patentes;
    }

    public static Funcao obterFuncaoPorNome(String nome) {
        return Arrays.stream(Funcao.values())
                .filter(f -> f.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Função não encontrada: " + nome));
    }

    public String getNome() {
        return nome;
    }

    public int getOrdemExibicao() {
        return ordemExibicao;
    }

    public List<Patente> getPatentes() {
        return patentes;
    }

}
