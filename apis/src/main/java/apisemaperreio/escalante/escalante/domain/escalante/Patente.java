package apisemaperreio.escalante.escalante.domain.escalante;

public enum Patente {

    TEN("Tenente"),
    SUBTEN("Subtenente"),
    SGT("Sargento"),
    CB("Cabo"),
    SD("Soldado");

    private String nome;

    private Patente(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

}
