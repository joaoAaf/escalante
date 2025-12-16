package br.com.appsemaperreio.escalante_api.escalante.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Cov extends AntigosPrimeiro {

    private final int FOLGA_COV = 4;

    public Cov(LocalDate dataServico) {
        super(dataServico, Funcao.COV);
        this.folga = FOLGA_COV;
    }

    public Cov(LocalDate dataServico, ServicoOperacional servicoOperacional) {
        super(dataServico, Funcao.COV);
        this.folga = servicoOperacional.getFolga();
        this.militar = servicoOperacional.getMilitar();
    }

    @Override
    public void escalarMilitar(Militar militar) {
        super.escalarMilitar(militar);
        ServicoOperacional.covFiscal = Optional.ofNullable(militar);
    }

    @Override
    public Optional<Militar> buscarMilitar(List<Militar> militares) {
        return selecionarMilitarApto(militares, true);
    }

    @Override
    public ServicoOperacional cloneDataSeguinte(ServicoOperacional servicoOperacional, Militar militar) {
        var proximoServico = new Cov(servicoOperacional.getDataServico().plusDays(1), servicoOperacional);
        militar.getUltimosServicos().add(proximoServico);
        return proximoServico;
    }

}
