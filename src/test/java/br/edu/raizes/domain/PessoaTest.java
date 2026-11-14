package br.edu.raizes.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class PessoaTest {
    @Test
    void cadaPessoaInformaOProprioPerfil() {
        List<Pessoa> pessoas =
                List.of(
                        new Administrador(1, "Administrador", "admin", "ADM-001"),
                        new Produtor(2, "Sítio", "Frutas", "4433334444", "Sarandi", List.of()));
        assertThat(pessoas)
                .extracting(Pessoa::obterTipoPerfil)
                .containsExactly("ADMINISTRADOR", "PRODUTOR");
    }
}
