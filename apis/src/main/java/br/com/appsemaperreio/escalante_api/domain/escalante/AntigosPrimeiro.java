package br.com.appsemaperreio.escalante_api.domain.escalante;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AntigosPrimeiro extends ServicoOperacional {

    public AntigosPrimeiro(LocalDate dataServico, Funcao funcao) {
        super(dataServico, funcao);
    }

    @Override
    protected Optional<Militar> selecionarMilitarApto(List<Militar> militares, Boolean cov) {
        var militaresAptos = filtrarMilitaresAptos(militares, cov);
        var militaresAptosNuncaEscalados = filtrarMilitaresAptosNuncaEscalados(militaresAptos);
        if (!militaresAptosNuncaEscalados.isEmpty())
            return filtrarMilitarAptoNuncaEscalado(militaresAptosNuncaEscalados);
        return filtrarMilitarAptoJaEscalado(militaresAptos);
    }

    @Override
    protected List<Militar> filtrarMilitaresAptos(List<Militar> militares, Boolean cov) {
        return militares.stream()
                .filter(militar -> militar.getCov().equals(cov) &&
                        !this.dataServico.equals(militar.getNascimento()))
                .collect(Collectors.toList());
    }

    private Optional<Militar> filtrarMilitarAptoNuncaEscalado(List<Militar> militares) {
        return militares.stream()
                .sorted(Comparator.comparingInt(Militar::getAntiguidade))
                .findFirst();
    }

    private Optional<Militar> filtrarMilitarAptoJaEscalado(List<Militar> militares) {
        return verificarFolgaMilitar(
                militares.stream()
                        .sorted(Comparator.comparing(Militar::dataUltimoServico)
                                .thenComparing(Comparator.comparingInt(Militar::getAntiguidade)))
                        .findFirst());
    }

}
