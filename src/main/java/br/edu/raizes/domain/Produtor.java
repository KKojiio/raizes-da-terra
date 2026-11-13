package br.edu.raizes.domain;

import java.util.List;

public final class Produtor extends ItemCatalogo {
    private final String descricao;
    private final String telefone;
    private final String localizacao;
    private final List<Produto> produtos;
    public Produtor(long id, String nome, String descricao, String telefone, String localizacao, List<Produto> produtos) {
        super(id, nome); this.descricao = descricao; this.telefone = telefone;
        this.localizacao = localizacao; this.produtos = List.copyOf(produtos);
    }
    public String getDescricao() { return descricao; }
    public String getTelefone() { return telefone; }
    public String getLocalizacao() { return localizacao; }
    public List<Produto> getProdutos() { return produtos; }
    @Override public String getResumo() { return localizacao + " - " + produtos.size() + " produto(s) no catálogo"; }
}
