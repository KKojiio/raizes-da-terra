package br.edu.raizes.domain;

/**
 * Produto comercializado por um produtor (RF03, RN03). Sempre pertence a um único produtor, que
 * não muda ao editar o produto.
 */
public final class Produto {
    private final long id;
    private final long produtorId;
    private String nome;
    private String descricao;
    private String categoria;

    public Produto(long id, long produtorId, String nome, String descricao, String categoria) {
        this.id = id;
        this.produtorId = produtorId;
        atualizarDados(nome, descricao, categoria);
    }

    /** Atualiza os dados do produto; a gravação no banco é feita pelo CatalogoService. */
    public void atualizarDados(String nome, String descricao, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    /** Consulta do produto (RF04): nome, descrição e categoria. */
    public String exibirInformacoes() {
        return nome + " - " + descricao + " (" + categoria + ")";
    }

    public long id() {
        return id;
    }

    public long produtorId() {
        return produtorId;
    }

    public String nome() {
        return nome;
    }

    public String descricao() {
        return descricao;
    }

    public String categoria() {
        return categoria;
    }
}
