package br.edu.raizes.domain;

/** Contrato comum usado pelo catálogo para exibir entidades diferentes. */
public abstract class ItemCatalogo {
    private final long id;
    private final String nome;
    protected ItemCatalogo(long id, String nome) { this.id = id; this.nome = nome; }
    public long getId() { return id; }
    public String getNome() { return nome; }
    public abstract String getResumo();
}
