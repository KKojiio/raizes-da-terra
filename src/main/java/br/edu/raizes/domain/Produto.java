package br.edu.raizes.domain;

public record Produto(long id, long produtorId, String nome, String descricao, String categoria) {}
