package br.edu.raizes.domain;

import java.util.List;

public final class Produtor implements ItemCatalogo {
    private final long id;
    private final String nome;
    private final String descricao;
    private final String telefone;
    private final String localizacao;
    private final List<Produto> produtos;

    public Produtor(
            long id,
            String nome,
            String descricao,
            String telefone,
            String localizacao,
            List<Produto> produtos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.telefone = telefone;
        this.localizacao = localizacao;
        this.produtos = List.copyOf(produtos);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    @Override
    public String getResumo() {
        return localizacao + " - " + produtos.size() + " produto(s) no catálogo";
    }
}
