package br.com.appsemaperreio.escalante_api.escalante.domain;

import java.time.LocalDate;

public class Permanente extends ModernosPrimeiro {

    private final int FOLGA_PERMANENTE = 3;

    public Permanente(LocalDate dataServico) {
        super(dataServico, Funcao.PERMANENTE);
        this.folga = FOLGA_PERMANENTE;
    }

    public Permanente(LocalDate dataServico, ServicoOperacional servicoOperacional) {
        super(dataServico, Funcao.PERMANENTE);
        this.folga = servicoOperacional.getFolga();
        this.militar = servicoOperacional.getMilitar();
    }

    @Override
    public ServicoOperacional cloneDataSeguinte(ServicoOperacional servicoOperacional, Militar militar) {
        var proximoServico = militar.getUltimosServicos().size() % 2 != 0
                ? new AjudanteLinha(servicoOperacional.getDataServico().plusDays(1), servicoOperacional)
                : new Permanente(servicoOperacional.getDataServico().plusDays(1), servicoOperacional);
        militar.getUltimosServicos().add(proximoServico);
        return proximoServico;
    }

}
