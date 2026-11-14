package br.edu.raizes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FeiraTest {
    private final Feira feira =
            new Feira(1, "Feira do Produtor", "Av. Brasil, 100", "Sábado", "07:00 às 12:00");

    @Test
    void exibirDetalhesMostraEnderecoEFuncionamento() {
        assertThat(feira.exibirDetalhes())
                .contains("Feira do Produtor", "Av. Brasil, 100", "Sábado", "07:00 às 12:00");
    }

    @Test
    void atualizarInformacoesTrocaOsDadosMantendoOId() {
        feira.atualizarInformacoes("Feira Nova", "Rua 2", "Domingo", "08:00 às 11:00");
        assertThat(feira.getId()).isEqualTo(1);
        assertThat(feira.getNome()).isEqualTo("Feira Nova");
        assertThat(feira.getResumo()).isEqualTo("Domingo, 08:00 às 11:00 - Rua 2");
    }

    @Test
    void feiraEProdutorSaoItensDoCatalogo() {
        ItemCatalogo item = feira;
        assertThat(item.getResumo()).contains("Sábado");
    }
}
