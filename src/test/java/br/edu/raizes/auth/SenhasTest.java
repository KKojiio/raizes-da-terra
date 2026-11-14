package br.edu.raizes.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SenhasTest {
    @Test
    void hashConfereSomenteComASenhaCerta() {
        String hash = Senhas.gerarHash("senha-de-teste-123");
        assertThat(hash).startsWith("pbkdf2$").doesNotContain("senha-de-teste-123");
        assertThat(Senhas.confere("senha-de-teste-123", hash)).isTrue();
        assertThat(Senhas.confere("senha-errada-1234", hash)).isFalse();
    }

    @Test
    void mesmaSenhaGeraHashesDiferentes() {
        assertThat(Senhas.gerarHash("igual-igual-123"))
                .isNotEqualTo(Senhas.gerarHash("igual-igual-123"));
    }
}
