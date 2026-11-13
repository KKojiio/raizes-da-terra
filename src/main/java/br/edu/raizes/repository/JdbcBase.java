package br.edu.raizes.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.*;
import java.util.Objects;

abstract class JdbcBase {
    protected final JdbcTemplate jdbc;

    protected JdbcBase(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected long inserir(String sql, Object... parametros) {
        var chaveGerada = new GeneratedKeyHolder();
        jdbc.update(
                conexao -> {
                    PreparedStatement comando =
                            conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    for (int i = 0; i < parametros.length; i++)
                        comando.setObject(i + 1, parametros[i]);
                    return comando;
                },
                chaveGerada);
        return Objects.requireNonNull(chaveGerada.getKey()).longValue();
    }

    protected void exigirRegistroAlterado(int linhasAlteradas) {
        if (linhasAlteradas != 1)
            throw new IllegalArgumentException("Registro não encontrado. Confira o identificador.");
    }
}
