package br.edu.raizes.service;

import java.text.Normalizer;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

final class FuncionamentoFeira {
    private static final List<String> DIAS =
            List.of("segunda", "terca", "quarta", "quinta", "sexta", "sabado", "domingo");
    private static final List<String> NOMES =
            List.of(
                    "Segunda-feira",
                    "Terça-feira",
                    "Quarta-feira",
                    "Quinta-feira",
                    "Sexta-feira",
                    "Sábado",
                    "Domingo");
    private static final Pattern HORA = Pattern.compile("(\\d{1,2})(?::(\\d{2})|h(\\d{2})?)?");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm");

    private FuncionamentoFeira() {}

    static String normalizarDias(String valor) {
        String texto = normalizar(valor);
        Set<String> dias = new LinkedHashSet<>();
        for (String parte : texto.split("\\s*(?:,|;|\\be\\b)\\s*", -1)) {
            String[] intervalo = parte.strip().split("\\s+(?:a|ate)\\s+", -1);
            if (intervalo.length > 2 || intervalo[0].isBlank()) throw diasInvalidos();
            int inicio = indiceDia(intervalo[0]);
            int fim = intervalo.length == 2 ? indiceDia(intervalo[1]) : inicio;
            int indice = inicio;
            while (true) {
                dias.add(NOMES.get(indice));
                if (indice == fim) break;
                indice = (indice + 1) % DIAS.size();
            }
        }
        return String.join(", ", dias);
    }

    static String normalizarHorario(String valor) {
        String[] partes = normalizar(valor).split("\\s*(?:ate|as|a|-)\\s*", -1);
        if (partes.length != 2) throw horarioInvalido();
        LocalTime inicio = lerHora(partes[0]);
        LocalTime fim = lerHora(partes[1]);
        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException(
                    "O horário final deve ser posterior ao inicial, no mesmo dia.");
        }
        return inicio.format(FORMATO) + " às " + fim.format(FORMATO);
    }

    private static int indiceDia(String dia) {
        String nome = dia.strip();
        if (nome.endsWith("-feira")) nome = nome.substring(0, nome.length() - 6);
        int indice = DIAS.indexOf(nome);
        if (indice < 0) throw diasInvalidos();
        return indice;
    }

    private static LocalTime lerHora(String texto) {
        var resultado = HORA.matcher(texto.strip());
        if (!resultado.matches()) throw horarioInvalido();
        String minutos = resultado.group(2) != null ? resultado.group(2) : resultado.group(3);
        try {
            return LocalTime.of(
                    Integer.parseInt(resultado.group(1)),
                    minutos == null ? 0 : Integer.parseInt(minutos));
        } catch (DateTimeException | NumberFormatException erro) {
            throw horarioInvalido();
        }
    }

    private static String normalizar(String valor) {
        if (valor == null) return "";
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .strip();
    }

    private static IllegalArgumentException diasInvalidos() {
        return new IllegalArgumentException(
                "Informe dias válidos, como sábado, terça e quinta ou segunda a sexta.");
    }

    private static IllegalArgumentException horarioInvalido() {
        return new IllegalArgumentException(
                "Informe o horário inicial e final, como 08:00 às 12:00.");
    }
}
