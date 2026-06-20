package br.com.appsemaperreio.escalante_api.escalante.model.domain;

public enum Patente {

    TEN("Tenente"),
    SUBTEN("Subtenente"),
    SGT("Sargento"),
    CB("Cabo"),
    SD("Soldado");

    private final String nome;

    private Patente(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

}
