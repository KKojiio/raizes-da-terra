package br.edu.raizes.domain;

/**
 * Interface do catálogo público: tudo o que o consumidor consulta (feiras e produtores). Cada
 * implementação define o próprio resumo, e o catálogo os trata juntos numa List<ItemCatalogo>
 * (polimorfismo via @Override).
 */
public interface ItemCatalogo {
    long getId();

    String getNome();

    String getResumo();
}
