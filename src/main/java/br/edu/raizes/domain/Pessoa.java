package br.edu.raizes.domain;

public abstract class Pessoa {
    private final long id;
    private final String nome;
    private final String telefone;

    protected Pessoa(long id, String nome, String telefone) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public abstract String obterTipoPerfil();
}
