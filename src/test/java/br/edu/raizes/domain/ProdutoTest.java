package br.edu.raizes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ProdutoTest {
    private final Produto produto = new Produto(1, 7, "Alface", "Maço", "Hortaliça");

    @Test
    void exibirInformacoesMostraNomeDescricaoECategoria() {
        assertThat(produto.exibirInformacoes()).isEqualTo("Alface - Maço (Hortaliça)");
    }

    @Test
    void atualizarDadosPreservaIdEProdutor() {
        produto.atualizarDados("Alface crespa", "Unidade", "Folhas");
        assertThat(produto.nome()).isEqualTo("Alface crespa");
        assertThat(produto.id()).isEqualTo(1);
        assertThat(produto.produtorId()).isEqualTo(7);
    }
}
