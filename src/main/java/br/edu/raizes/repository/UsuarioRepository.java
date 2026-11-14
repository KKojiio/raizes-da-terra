package br.edu.raizes.repository;

import br.edu.raizes.domain.Administrador;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class UsuarioRepository extends JdbcBase {
    private static final String ADMINISTRADOR_POR_EMAIL =
            "SELECT u.id, u.nome, u.email, u.senha_hash, a.matricula FROM usuario u"
                    + " JOIN administrador a ON a.id_usuario=u.id"
                    + " WHERE u.email=? AND u.tipo='ADMINISTRADOR'";

    public UsuarioRepository(JdbcTemplate jdbc) {
        super(jdbc);
    }

    public long contarAdministradores() {
        Long total = jdbc.queryForObject("SELECT COUNT(*) FROM administrador", Long.class);
        return total == null ? 0 : total;
    }

    public Optional<Administrador> administrador(String email) {
        return jdbc
                .query(
                        ADMINISTRADOR_POR_EMAIL,
                        (r, i) ->
                                new Administrador(
                                        r.getLong("id"),
                                        r.getString("nome"),
                                        r.getString("email"),
                                        r.getString("matricula")),
                        email)
                .stream()
                .findFirst();
    }

    public Optional<String> senhaHashDoAdministrador(String email) {
        return jdbc.query(ADMINISTRADOR_POR_EMAIL, (r, i) -> r.getString("senha_hash"), email)
                .stream()
                .findFirst();
    }

    @Transactional
    public long criarAdministrador(String nome, String email, String senhaHash, String matricula) {
        long id =
                inserir(
                        "INSERT INTO usuario(nome,email,senha_hash,tipo) VALUES (?,?,?,'ADMINISTRADOR')",
                        nome,
                        email,
                        senhaHash);
        jdbc.update("INSERT INTO administrador(id_usuario,matricula) VALUES (?,?)", id, matricula);
        return id;
    }
}
