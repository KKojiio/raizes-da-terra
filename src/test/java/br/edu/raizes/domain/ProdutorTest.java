package br.edu.raizes.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class ProdutorTest {
    private final Produtor produtor =
            new Produtor(7, "Sítio Boa Terra", "Hortaliças", "44999990000", "Maringá", List.of());

    @Test
    void produtorEUmaPessoaComPerfilProprio() {
        Pessoa pessoa = produtor;
        assertThat(pessoa.obterTipoPerfil()).isEqualTo("PRODUTOR");
        assertThat(pessoa.getTelefone()).isEqualTo("44999990000");
    }

    @Test
    void adicionarERemoverProduto() {
        var alface = new Produto(1, 7, "Alface", "Maço", "Hortaliça");
        produtor.adicionarProduto(alface);
        assertThat(produtor.getProdutos()).hasSize(1);
        produtor.removerProduto(alface);
        assertThat(produtor.getProdutos()).isEmpty();
    }

    @Test
    void naoAceitaProdutoDeOutroProdutor() {
        assertThatThrownBy(() -> produtor.adicionarProduto(new Produto(2, 99, "A", "B", "C")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
