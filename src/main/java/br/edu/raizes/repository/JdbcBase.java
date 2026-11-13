package br.edu.raizes.repository;
import java.sql.*;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

abstract class JdbcBase {
    protected final JdbcTemplate jdbc;
    protected JdbcBase(JdbcTemplate jdbc) { this.jdbc=jdbc; }
    protected long inserir(String sql,Object... args) {
        var key=new GeneratedKeyHolder();
        jdbc.update(c -> {
            PreparedStatement stmt=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            for(int i=0;i<args.length;i++) stmt.setObject(i+1,args[i]);
            return stmt;
        },key);
        return Objects.requireNonNull(key.getKey()).longValue();
    }
    protected void mudou(int n) { if(n!=1) throw new IllegalArgumentException("Registro não encontrado. Confira o identificador."); }
}
