package br.com.appsemaperreio.escalante_api.escalante.model.domain;

import java.time.LocalDate;

public class OperadorLinha extends ModernosPrimeiro {

    private final int FOLGA_OPERADOR_LINHA = 3;
    private int linha;

    public OperadorLinha(LocalDate dataServico) {
        this(dataServico, 1);
    }

    public OperadorLinha(LocalDate dataServico, int linha) {
        super(dataServico, Funcao.OPERADOR_DE_LINHA);
        this.folga = FOLGA_OPERADOR_LINHA;
        this.linha = linha;
    }

    public OperadorLinha(LocalDate dataServico, ServicoOperacional servicoOperacional, int linha) {
        super(dataServico, Funcao.OPERADOR_DE_LINHA);
        this.folga = servicoOperacional.getFolga();
        this.militar = servicoOperacional.getMilitar();
        this.linha = linha;
    }

    @Override
    public ServicoOperacional cloneDataSeguinte(ServicoOperacional servicoOperacional, Militar militar) {
        var origem = (OperadorLinha) servicoOperacional;
        var proximoServico = new OperadorLinha(
                servicoOperacional.getDataServico().plusDays(1),
                servicoOperacional,
                origem.getLinha());
        militar.getUltimosServicos().add(proximoServico);
        return proximoServico;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

}
