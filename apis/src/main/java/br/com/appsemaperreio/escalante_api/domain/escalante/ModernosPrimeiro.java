package br.com.appsemaperreio.escalante_api.domain.escalante;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class ModernosPrimeiro extends ServicoOperacional {

    public ModernosPrimeiro(LocalDate dataServico, Funcao funcao) {
        super(dataServico, funcao);
    }

    @Override
    public Optional<Militar> buscarMilitar(List<Militar> militares) {
        return selecionarMilitarApto(militares, false).or(() -> selecionarMilitarApto(militares, true));
    }

    @Override
    protected Optional<Militar> selecionarMilitarApto(List<Militar> militares, Boolean cov) {
        var militaresAptos = filtrarMilitaresAptos(militares, cov);
        var militaresAptosNuncaEscalados = filtrarMilitaresAptosNuncaEscalados(militaresAptos);
        if (!militaresAptosNuncaEscalados.isEmpty())
            return filtrarMilitarApto(militaresAptosNuncaEscalados, this::filtrarMilitarAptoNuncaEscalado);
        return filtrarMilitarApto(militaresAptos, this::filtrarMilitarAptoJaEscalado);
    }

    @Override
    protected List<Militar> filtrarMilitaresAptos(List<Militar> militares, Boolean cov) {
        return militares.stream()
                .filter(militar -> militar.getCov().equals(cov) &&
                        this.funcao.getPatentes().contains(militar.getPatente()) &&
                        !this.dataServico.equals(militar.getNascimento()))
                .collect(Collectors.toList());
    }

    private Optional<Militar> filtrarMilitarApto(List<Militar> militares,
            BiFunction<List<Militar>, Patente, Optional<Militar>> filtro) {
        for (var patente : this.funcao.getPatentes()) {
            var militarApto = filtro.apply(militares, patente);
            if (militarApto.isPresent())
                return militarApto;
        }
        return Optional.empty();
    }

    private Optional<Militar> filtrarMilitarAptoNuncaEscalado(List<Militar> militares, Patente patente) {
        return militares.stream()
                .filter(militar -> militar.getPatente().equals(patente))
                .sorted(Comparator.comparingInt(Militar::getAntiguidade).reversed())
                .findFirst();
    }

    private Optional<Militar> filtrarMilitarAptoJaEscalado(List<Militar> militares, Patente patente) {
        return verificarFolgaMilitar(
                militares.stream()
                        .filter(militar -> militar.getPatente().equals(patente))
                        .sorted(Comparator.comparing(Militar::dataUltimoServico)
                                .thenComparing(Comparator.comparingInt(Militar::getAntiguidade).reversed()))
                        .findFirst());
    }

}
