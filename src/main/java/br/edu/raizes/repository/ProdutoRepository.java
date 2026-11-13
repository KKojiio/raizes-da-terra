package br.edu.raizes.repository;

import br.edu.raizes.domain.*;

import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class ProdutoRepository extends JdbcBase {
    static final RowMapper<Produto> MAPPER =
            (resultado, indice) ->
                    new Produto(
                            resultado.getLong("id"),
                            resultado.getLong("produtor_id"),
                            resultado.getString("nome"),
                            resultado.getString("descricao"),
                            resultado.getString("categoria"));

    public ProdutoRepository(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public List<Produto> produtos() {
        return jdbc.query("SELECT * FROM produto ORDER BY nome,id", ProdutoRepository.MAPPER);
    }

    public Produto produto(long id) {
        return jdbc.query("SELECT * FROM produto WHERE id=?", ProdutoRepository.MAPPER, id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));
    }

    public long salvarProduto(
            long id, long produtorId, String nome, String descricao, String categoria) {
        if (id == 0)
            return inserir(
                    "INSERT INTO produto(produtor_id,nome,descricao,categoria) VALUES (?,?,?,?)",
                    produtorId,
                    nome,
                    descricao,
                    categoria);

        exigirRegistroAlterado(
                jdbc.update(
                        "UPDATE produto SET nome=?,descricao=?,categoria=? WHERE id=? AND"
                                + " produtor_id=?",
                        nome,
                        descricao,
                        categoria,
                        id,
                        produtorId));
        return id;
    }

    public void excluirProduto(long id) {
        exigirRegistroAlterado(jdbc.update("DELETE FROM produto WHERE id=?", id));
    }
}
