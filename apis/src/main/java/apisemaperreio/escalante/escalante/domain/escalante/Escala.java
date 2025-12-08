package apisemaperreio.escalante.escalante.domain.escalante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import apisemaperreio.escalante.escalante.utils.factories.ServicoOperacionalFactory;

public class Escala {

    private DadosEscala dadosEscala;
    private List<ServicoOperacional> servicosEscala = new ArrayList<ServicoOperacional>();

    public Escala(DadosEscala dadosEscala) {
        this.dadosEscala = dadosEscala;
    }

    public void preencherEscala(List<Militar> militares) {
        var dataAtual = this.dadosEscala.dataInicio();
        var funcoes = Arrays.stream(Funcao.values()).toList();
        while (dataAtual.compareTo(this.dadosEscala.dataFim()) <= 0) {
            for (var funcao : funcoes)
                buscarMilitarApto(militares, ServicoOperacionalFactory.criarServicoOperacional(funcao, dataAtual));
            dataAtual = dataAtual.plusDays(this.dadosEscala.diasServico());
        }
        this.servicosEscala.sort(Comparator.comparing(ServicoOperacional::getDataServico)
                .thenComparing(ServicoOperacional::getFuncao, Comparator.comparingInt(Funcao::getOrdemExibicao))
                .thenComparing(ServicoOperacional::getMilitar, Comparator.comparingInt(Militar::getAntiguidade)));
    }

    private void buscarMilitarApto(List<Militar> militares, ServicoOperacional servicoOperacional) {
        var militarEscalado = servicoOperacional.buscarMilitar(militares);
        escalarMilitar(servicoOperacional, militarEscalado);
        if (servicoOperacional.covEhFiscal() && servicoOperacional instanceof AjudanteLinha) {
            buscarMilitarApto(militares, ServicoOperacionalFactory.criarServicoOperacional(Funcao.OPERADOR_DE_LINHA,
                    servicoOperacional.getDataServico()));
        }
    }

    private void escalarMilitar(ServicoOperacional servicoOperacional, Optional<Militar> militarEscalado) {
        militarEscalado.ifPresent(militar -> {
            servicoOperacional.escalarMilitar(militar);
            preencherDiasServico(servicoOperacional, militar);
        });

    }

    private void preencherDiasServico(ServicoOperacional servicoOperacional, Militar militarEscalado) {
        this.servicosEscala.add(servicoOperacional);
        for (int diaServico = 2; diaServico <= this.dadosEscala.diasServico(); diaServico++) {
            this.servicosEscala.add(servicoOperacional.cloneDataSeguinte(servicoOperacional, militarEscalado));
        }
    }

    public DadosEscala getDadosEscala() {
        return dadosEscala;
    }

    public List<ServicoOperacional> getServicosEscala() {
        return servicosEscala;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dadosEscala == null) ? 0 : dadosEscala.hashCode());
        result = prime * result + ((servicosEscala == null) ? 0 : servicosEscala.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Escala other = (Escala) obj;
        if (dadosEscala == null) {
            if (other.dadosEscala != null)
                return false;
        } else if (!dadosEscala.equals(other.dadosEscala))
            return false;
        if (servicosEscala == null) {
            if (other.servicosEscala != null)
                return false;
        } else if (!servicosEscala.equals(other.servicosEscala))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Escala [dataInicio=" + dadosEscala.dataInicio() + ", dataFim=" + dadosEscala.dataFim()
                + ", diasServico=" + dadosEscala.diasServico() + "]";
    }

}
