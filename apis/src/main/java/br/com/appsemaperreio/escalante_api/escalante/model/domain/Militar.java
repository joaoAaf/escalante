package br.com.appsemaperreio.escalante_api.escalante.model.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
public class Militar {

    @Id
    private String matricula;

    @Column(nullable = false, length = 40)
    private String nomePaz;

    @Column(nullable = false)
    private LocalDate nascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Patente patente;

    @Column(nullable = false, unique = true)
    private Integer antiguidade;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private int folgaEspecial = 0;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean cov = false;

    @Transient
    private List<ServicoOperacional> ultimosServicos = new ArrayList<>();

    public Militar() {
    }

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

    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNomePaz() {
        return nomePaz;
    }

    public void setNomePaz(String nomePaz) { this.nomePaz = nomePaz; }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) { this.nascimento = nascimento; }

    public Patente getPatente() {
        return patente;
    }

    public void setPatente(Patente patente) { this.patente = patente; }

    public Integer getAntiguidade() {
        return antiguidade;
    }

    public void setAntiguidade(Integer antiguidade) { this.antiguidade = antiguidade; }

    public int getFolgaEspecial() {
        return folgaEspecial;
    }

    public void setFolgaEspecial(int folgaEspecial) { this.folgaEspecial = folgaEspecial; }

    public Boolean getCov() {
        return cov;
    }

    public void setCov(Boolean cov) { this.cov = cov; }

    public List<ServicoOperacional> getUltimosServicos() {
        return ultimosServicos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Militar militar = (Militar) o;

        return folgaEspecial == militar.folgaEspecial &&
                nomePaz.equals(militar.nomePaz) &&
                nascimento.equals(militar.nascimento) &&
                patente == militar.patente &&
                antiguidade.equals(militar.antiguidade) &&
                cov.equals(militar.cov);
    }

    @Override
    public int hashCode() {
        int result = nomePaz.hashCode();
        result = 31 * result + nascimento.hashCode();
        result = 31 * result + patente.hashCode();
        result = 31 * result + antiguidade.hashCode();
        result = 31 * result + folgaEspecial;
        result = 31 * result + cov.hashCode();
        return result;
    }
}
