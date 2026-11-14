package br.edu.raizes.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Produtor extends Pessoa implements ItemCatalogo {
    private final String descricao;
    private final String localizacao;
    private final List<Produto> produtos = new ArrayList<>();

    public Produtor(
            long id,
            String nome,
            String descricao,
            String telefone,
            String localizacao,
            List<Produto> produtos) {
        super(id, nome, telefone);
        this.descricao = descricao;
        this.localizacao = localizacao;
        for (Produto produto : produtos) adicionarProduto(produto);
    }

    public void adicionarProduto(Produto produto) {
        if (produto.produtorId() != getId())
            throw new IllegalArgumentException("O produto pertence a outro produtor.");
        produtos.add(produto);
    }

    public void removerProduto(Produto produto) {
        produtos.removeIf(atual -> atual.id() == produto.id());
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    @Override
    public String obterTipoPerfil() {
        return "PRODUTOR";
    }

    @Override
    public String getResumo() {
        return localizacao + " - " + produtos.size() + " produto(s) no catálogo";
    }
}
