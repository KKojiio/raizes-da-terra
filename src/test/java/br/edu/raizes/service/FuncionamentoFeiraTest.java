package br.edu.raizes.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FuncionamentoFeiraTest {
    @Test
    void normalizaDiasComAcentosERepeticoes() {
        assertThat(FuncionamentoFeira.normalizarDias("terça-feira e quinta, terça"))
                .isEqualTo("Terça-feira, Quinta-feira");
    }

    @Test
    void aceitaIntervaloEntreDias() {
        assertThat(FuncionamentoFeira.normalizarDias("sexta até segunda"))
                .isEqualTo("Sexta-feira, Sábado, Domingo, Segunda-feira");
    }

    @ParameterizedTest
    @ValueSource(strings = {"45", "ontem", "", "sábado,", "segunda a", "terca-feira lixo"})
    void rejeitaDiasInvalidos(String valor) {
        assertThatThrownBy(() -> FuncionamentoFeira.normalizarDias(valor))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"8h às 12h", "08:00 às 12:00", "8-12", "8 a 12", "8 até 12"})
    void normalizaIntervalosValidos(String valor) {
        assertThat(FuncionamentoFeira.normalizarHorario(valor)).isEqualTo("08:00 às 12:00");
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "4:50",
                "25:00 às 26:00",
                "08:80 às 12:00",
                "12h às 8h",
                "8h às 8h",
                "",
                "amanhã",
                "08:00 às"
            })
    void rejeitaHorariosInvalidos(String valor) {
        assertThatThrownBy(() -> FuncionamentoFeira.normalizarHorario(valor))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
