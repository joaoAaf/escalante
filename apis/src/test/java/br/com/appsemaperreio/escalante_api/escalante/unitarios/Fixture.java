package br.com.appsemaperreio.escalante_api.escalante.unitarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Funcao;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.Patente;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.ServicoOperacional;
import br.com.appsemaperreio.escalante_api.escalante.model.domain.factories.ServicoOperacionalFactory;

final class Fixture {

    private Fixture() {}

    static List<Militar> criarMilitares() {
        var lista = new ArrayList<Militar>();
        lista.add(new Militar("89010001", "ANTONIO SILVA", LocalDate.of(1978, 4, 15), Patente.TEN, 1, 4, false));
        lista.add(new Militar("89010002", "SERGIO OLIVEIRA", LocalDate.of(1982, 6, 22), Patente.SGT, 2, 0, true));
        lista.add(new Militar("89010003", "PEDRO ALMEIDA", LocalDate.of(1985, 11, 8), Patente.SGT, 3, 0, true));
        lista.add(new Militar("89010004", "JOAO BATISTA", LocalDate.of(1983, 8, 14), Patente.SGT, 4, 14, false));
        lista.add(new Militar("89010005", "ANTONIO SILVA", LocalDate.of(1986, 10, 3), Patente.SGT, 5, 0, false));
        lista.add(new Militar("89010006", "FERNANDO COSTA", LocalDate.of(1987, 1, 12), Patente.CB, 6, 4, false));
        lista.add(new Militar("89010007", "MARCELO PEREIRA", LocalDate.of(1992, 3, 25), Patente.CB, 7, 0, true));
        lista.add(new Militar("89010008", "JULIANO CARVALHO", LocalDate.of(1993, 6, 17), Patente.CB, 8, 0, false));
        lista.add(new Militar("89010009", "ROBERTO SOUZA", LocalDate.of(1992, 9, 5), Patente.CB, 9, 0, false));
        lista.add(new Militar("89010010", "ALEXANDRE LIMA", LocalDate.of(1991, 4, 19), Patente.CB, 10, 0, true));
        lista.add(new Militar("89010011", "BRAULIO GOMES", LocalDate.of(1996, 1, 2), Patente.CB, 11, 0, false));
        lista.add(new Militar("89010012", "RODRIGO SANTOS", LocalDate.of(1986, 1, 23), Patente.CB, 12, 0, false));
        lista.add(new Militar("89010013", "VICTOR HENRIQUE", LocalDate.of(1989, 6, 11), Patente.CB, 13, 14, false));
        lista.add(new Militar("89010014", "RUBENS FERNANDES", LocalDate.of(1986, 9, 23), Patente.CB, 14, 0, false));
        lista.add(new Militar("89010015", "EDUARDO REIS", LocalDate.of(1991, 7, 14), Patente.CB, 15, 0, false));
        lista.add(new Militar("89010016", "GILSON MOURA", LocalDate.of(1988, 11, 28), Patente.CB, 16, 0, false));
        lista.add(new Militar("89010017", "DENNIS ROCHA", LocalDate.of(1988, 2, 7), Patente.SD, 17, 0, false));
        lista.add(new Militar("89010018", "RICARDO NUNES", LocalDate.of(1990, 8, 4), Patente.SD, 18, 0, false));
        lista.add(new Militar("89010019", "JOAO BATISTA", LocalDate.of(1991, 5, 16), Patente.SD, 19, 0, false));
        lista.add(new Militar("89010020", "LUIS FERREIRA", LocalDate.of(1988, 12, 21), Patente.SD, 20, 0, false));
        lista.add(new Militar("89010021", "CARLOS MOURA", LocalDate.of(1992, 4, 12), Patente.SD, 21, 0, false));
        lista.add(new Militar("89010022", "FELIPE SANTANA", LocalDate.of(1992, 3, 13), Patente.SD, 22, 0, true));
        lista.add(new Militar("89010023", "GUSTAVO BARBOSA", LocalDate.of(1993, 11, 2), Patente.SD, 23, 0, true));
        lista.add(new Militar("89010024", "FABIO ANDRADE", LocalDate.of(1987, 2, 18), Patente.SD, 24, 0, true));
        lista.add(new Militar("89010025", "THIAGO MENDES", LocalDate.of(1988, 11, 6), Patente.SD, 25, 0, false));
        return lista;
    }

    static List<ServicoOperacional> criarServicosAnteriores(List<Militar> militares) {
        var servicos = new ArrayList<ServicoOperacional>();
        servicos.add(criar(LocalDate.of(2026, 5, 1), "89010009", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 1), "89010024", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 1), "89010011", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 1), "89010018", Funcao.AJUDANTE_DE_LINHA, 2, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 1), "89010021", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 2), "89010009", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 2), "89010024", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 2), "89010018", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 3), "89010009", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 3), "89010008", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 3), "89010010", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 3), "89010014", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 3), "89010025", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 3), "89010017", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 4), "89010008", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 4), "89010010", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 4), "89010001", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 4), "89010015", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 4), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 4), "89010025", Funcao.OPERADOR_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 5), "89010021", Funcao.PERMANENTE, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 5), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 5), "89010007", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 5), "89010019", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 5), "89010006", Funcao.FISCAL_DE_DIA, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 6), "89010023", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 6), "89010019", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 6), "89010007", Funcao.OPERADOR_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 7), "89010015", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 7), "89010002", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 7), "89010016", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 7), "89010009", Funcao.AJUDANTE_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 8), "89010003", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 8), "89010010", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 8), "89010005", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 9), "89010005", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 9), "89010009", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 9), "89010018", Funcao.AJUDANTE_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 10), "89010009", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 10), "89010018", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 11), "89010009", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 11), "89010018", Funcao.PERMANENTE, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 12), "89010015", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 12), "89010024", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 12), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 12), "89010024", Funcao.COV, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 13), "89010008", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 13), "89010016", Funcao.COV, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 14), "89010019", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 14), "89010010", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 14), "89010019", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 14), "89010014", Funcao.OPERADOR_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 15), "89010006", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 15), "89010012", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 15), "89010013", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 15), "89010016", Funcao.AJUDANTE_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 15), "89010005", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 16), "89010009", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 16), "89010012", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 16), "89010013", Funcao.AJUDANTE_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 17), "89010001", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 17), "89010015", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 17), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 17), "89010024", Funcao.COV, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 18), "89010011", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 18), "89010009", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 18), "89010018", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 18), "89010016", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 18), "89010005", Funcao.COV, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 19), "89010003", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 19), "89010005", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 19), "89010018", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 19), "89010025", Funcao.OPERADOR_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 20), "89010018", Funcao.AJUDANTE_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 20), "89010025", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 20), "89010015", Funcao.OPERADOR_DE_LINHA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 20), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 20), "89010025", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 21), "89010024", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 21), "89010014", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 21), "89010019", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 22), "89010024", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 22), "89010019", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 22), "89010007", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 22), "89010010", Funcao.FISCAL_DE_DIA, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 23), "89010017", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 23), "89010012", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 23), "89010016", Funcao.AJUDANTE_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 23), "89010010", Funcao.COV, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 24), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 24), "89010012", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 24), "89010014", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 24), "89010005", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 24), "89010006", Funcao.FISCAL_DE_DIA, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 25), "89010023", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 25), "89010011", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 25), "89010009", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 25), "89010021", Funcao.AJUDANTE_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 26), "89010023", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 26), "89010011", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 26), "89010009", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 26), "89010018", Funcao.AJUDANTE_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 27), "89010015", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 27), "89010005", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 27), "89010017", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 27), "89010016", Funcao.FISCAL_DE_DIA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 28), "89010005", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 28), "89010002", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 28), "89010025", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 28), "89010002", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 28), "89010015", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 28), "89010017", Funcao.PERMANENTE, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 29), "89010003", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 29), "89010005", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 29), "89010008", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 29), "89010014", Funcao.OPERADOR_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 30), "89010019", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 30), "89010003", Funcao.AJUDANTE_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 30), "89010003", Funcao.OPERADOR_DE_LINHA, 3, militares));

        servicos.add(criar(LocalDate.of(2026, 5, 31), "89010009", Funcao.PERMANENTE, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 31), "89010001", Funcao.FISCAL_DE_DIA, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 31), "89010024", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 31), "89010012", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 5, 31), "89010001", Funcao.FISCAL_DE_DIA, 4, militares));

        servicos.add(criar(LocalDate.of(2026, 6, 1), "89010001", Funcao.FISCAL_DE_DIA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 6, 1), "89010007", Funcao.OPERADOR_DE_LINHA, 3, militares));
        servicos.add(criar(LocalDate.of(2026, 6, 1), "89010012", Funcao.COV, 4, militares));
        servicos.add(criar(LocalDate.of(2026, 6, 1), "89010016", Funcao.OPERADOR_DE_LINHA, 3, militares));

        return servicos;
    }

    private static ServicoOperacional criar(LocalDate data, String matricula, Funcao funcao, int folga,
            List<Militar> militares) {
        var servico = ServicoOperacionalFactory.criarServicoOperacional(funcao, data);
        var militar = militares.stream()
                .filter(m -> m.getMatricula().equals(matricula))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Militar " + matricula + " não encontrado"));
        servico.setMilitar(militar);
        return servico;
    }
}
