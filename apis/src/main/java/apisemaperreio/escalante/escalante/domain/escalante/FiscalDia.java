package apisemaperreio.escalante.escalante.domain.escalante;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FiscalDia extends AntigosPrimeiro {

    private final int FOLGA_FISCAL = 4;

    public FiscalDia(LocalDate dataServico) {
        super(dataServico, Funcao.FISCAL_DE_DIA);
        this.folga = FOLGA_FISCAL;
    }

    public FiscalDia(LocalDate dataServico, ServicoOperacional servicoOperacional) {
        super(dataServico, Funcao.FISCAL_DE_DIA);
        this.folga = servicoOperacional.getFolga();
        this.militar = servicoOperacional.getMilitar();
    }

    @Override
    public Optional<Militar> buscarMilitar(List<Militar> militares) {
        return verificarCovFiscal(selecionarMilitarApto(militares, false));
    }

    private Optional<Militar> verificarCovFiscal(Optional<Militar> militar) {
        if (covFiscal.isEmpty() || militar.isEmpty())
            return covFiscal.or(() -> militar);
        if (militar.get().getAntiguidade() < covFiscal.get().getAntiguidade()) {
            ServicoOperacional.covFiscal = Optional.empty();
            return militar;
        }
        return ServicoOperacional.covFiscal;
    }

    @Override
    public ServicoOperacional cloneDataSeguinte(ServicoOperacional servicoOperacional, Militar militar) {
        var proximoServico = new FiscalDia(servicoOperacional.getDataServico().plusDays(1), servicoOperacional);
        militar.getUltimosServicos().add(proximoServico);
        return proximoServico;
    }

}
