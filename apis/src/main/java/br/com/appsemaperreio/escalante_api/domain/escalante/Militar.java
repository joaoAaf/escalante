package br.com.appsemaperreio.escalante_api.domain.escalante;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Militar {

    private String matricula;
    private String nomePaz;
    private LocalDate nascimento;
    private Patente patente;
    private Integer antiguidade;
    private int folgaEspecial;
    private Boolean cov;
    @JsonIgnore
    private List<ServicoOperacional> ultimosServicos = new ArrayList<>();

    public Militar(String matricula, String nomePaz, LocalDate nascimento, Patente patente, Integer antiguidade,
            int folgaEspecial, Boolean cov) {
        this.matricula = matricula;
        this.nomePaz = nomePaz;
        this.nascimento = nascimento;
        this.patente = patente;
        this.antiguidade = antiguidade;
        this.folgaEspecial = folgaEspecial;
        this.cov = cov;
    }

    public void ultimosServicosConsecutivos(List<ServicoOperacional> servicos) {
        var servicosMilitar = servicos.stream()
                .filter(servico -> servico.getMilitar().equals(this))
                .sorted(Comparator.comparing(ServicoOperacional::getDataServico).reversed())
                .toList();
        if (servicosMilitar.isEmpty())
            return;
        for (int contador = 0; contador < servicosMilitar.size(); contador++) {
            if (contador > 0 && !servicosSaoConsecutivos(servicosMilitar, contador))
                break;
            this.ultimosServicos.add(servicosMilitar.get(contador));
        }
    }

    public boolean servicosSaoConsecutivos(List<ServicoOperacional> servicos, int contador) {
        return servicos.get(contador - 1).getDataServico()
                .equals(servicos.get(contador).getDataServico().plusDays(1));
    }

    public LocalDate dataUltimoServico() {
        return ultimosServicos.getFirst().getDataServico();
    }

    public int folgaUltimoServico() {
        return ultimosServicos.getFirst().getFolga();
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNomePaz() {
        return nomePaz;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public Patente getPatente() {
        return patente;
    }

    public Integer getAntiguidade() {
        return antiguidade;
    }

    public int getFolgaEspecial() {
        return folgaEspecial;
    }

    public Boolean getCov() {
        return cov;
    }

    public List<ServicoOperacional> getUltimosServicos() {
        return ultimosServicos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
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
        Militar other = (Militar) obj;
        if (matricula == null) {
            if (other.matricula != null)
                return false;
        } else if (!matricula.equals(other.matricula))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Militar [matricula=" + matricula + ", nomePaz=" + nomePaz + ", nascimento=" + nascimento +
                ", patente=" + patente + ", antiguidade=" + antiguidade + ", folgaEspecial=" +
                folgaEspecial + ", cov=" + cov + "]";
    }

}
