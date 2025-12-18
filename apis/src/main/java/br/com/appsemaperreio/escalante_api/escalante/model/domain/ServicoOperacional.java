package br.com.appsemaperreio.escalante_api.escalante.model.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ServicoOperacional {

    protected static Optional<Militar> covFiscal = Optional.empty();

    @JsonIgnore
    private Long id;
    protected LocalDate dataServico;
    protected Funcao funcao;
    protected int folga;
    protected Militar militar;

    public ServicoOperacional(LocalDate dataServico, Funcao funcao) {
        this.dataServico = dataServico;
        this.funcao = funcao;
    }

    public Boolean covEhFiscal() {
        return covFiscal.isPresent();
    }

    public abstract Optional<Militar> buscarMilitar(List<Militar> militares);

    public void escalarMilitar(Militar militar) {
        this.folga = definirFolga(militar.getFolgaEspecial(), this.folga);
        this.militar = militar;
        militar.getUltimosServicos().clear();
        militar.getUltimosServicos().add(this);
    }

    protected int definirFolga(int folgaMilitar, int folgaServico) {
        return folgaMilitar > folgaServico ? folgaMilitar : folgaServico;
    }

    protected abstract Optional<Militar> selecionarMilitarApto(List<Militar> militares, Boolean cov);

    protected abstract List<Militar> filtrarMilitaresAptos(List<Militar> militares, Boolean cov);

    protected List<Militar> filtrarMilitaresAptosNuncaEscalados(List<Militar> militares) {
        return militares.stream()
                .filter(militar -> militar.getUltimosServicos().isEmpty())
                .collect(Collectors.toList());
    }

    protected Optional<Militar> verificarFolgaMilitar(Optional<Militar> militar) {
        if (militar.isEmpty())
            return militar;
        var militarVerificado = militar.get();
        var folga = militarVerificado.getUltimosServicos().size() * militarVerificado.folgaUltimoServico();
        return militarVerificado.dataUltimoServico().plusDays(folga + 1).isAfter(this.getDataServico())
                ? Optional.empty()
                : militar;
    }

    public abstract ServicoOperacional cloneDataSeguinte(ServicoOperacional servicoOperacional, Militar militar);

    public Long getId() {
        return id;
    }

    public LocalDate getDataServico() {
        return dataServico;
    }

    public Funcao getFuncao() {
        return funcao;
    }

    public int getFolga() {
        return folga;
    }

    public Militar getMilitar() {
        return militar;
    }

    public void setMilitar(Militar militar) {
        this.militar = militar;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((dataServico == null) ? 0 : dataServico.hashCode());
        result = prime * result + ((funcao == null) ? 0 : funcao.hashCode());
        result = prime * result + folga;
        result = prime * result + ((militar == null) ? 0 : militar.hashCode());
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
        ServicoOperacional other = (ServicoOperacional) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (dataServico == null) {
            if (other.dataServico != null)
                return false;
        } else if (!dataServico.equals(other.dataServico))
            return false;
        if (funcao != other.funcao)
            return false;
        if (folga != other.folga)
            return false;
        if (militar == null) {
            if (other.militar != null)
                return false;
        } else if (!militar.equals(other.militar))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServicoOperacional [id=" + id + ", dataServico=" + dataServico + ", funcao=" + funcao + ", folga="
                + folga + ", matriculaEscalado=" + militar.getMatricula() + ", patenteEscalado=" + militar.getPatente()
                + ", antiguidadeEscalado=" + militar.getAntiguidade() + "]";
    }

}
