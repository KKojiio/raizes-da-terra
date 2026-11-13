package br.edu.raizes.repository;

import br.edu.raizes.domain.*;

import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class FeiraRepository extends JdbcBase {
    static final RowMapper<Feira> MAPPER =
            (resultado, indice) ->
                    new Feira(
                            resultado.getLong("id"),
                            resultado.getString("nome"),
                            resultado.getString("endereco"),
                            resultado.getString("dias"),
                            resultado.getString("horario"));
    private final ProdutorRepository produtores;

    public FeiraRepository(JdbcTemplate jdbc, ProdutorRepository produtores) {
        super(jdbc);
        this.produtores = produtores;
    }

    public List<Feira> feiras() {
        return jdbc.query("SELECT * FROM feira ORDER BY nome,id", FeiraRepository.MAPPER);
    }

    public Feira feira(long id) {
        return jdbc.query("SELECT * FROM feira WHERE id=?", FeiraRepository.MAPPER, id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Feira não encontrada."));
    }

    public long salvarFeira(long id, String nome, String endereco, String dias, String horario) {
        if (id == 0)
            return inserir(
                    "INSERT INTO feira(nome,endereco,dias,horario) VALUES (?,?,?,?)",
                    nome,
                    endereco,
                    dias,
                    horario);
        exigirRegistroAlterado(
                jdbc.update(
                        "UPDATE feira SET nome=?,endereco=?,dias=?,horario=? WHERE id=?",
                        nome,
                        endereco,
                        dias,
                        horario,
                        id));
        return id;
    }

    public void excluirFeira(long id) {
        exigirRegistroAlterado(jdbc.update("DELETE FROM feira WHERE id=?", id));
    }

    public List<Produtor> produtoresDaFeira(long id) {
        return jdbc.query(
                "SELECT p.* FROM produtor p JOIN produtor_feira pf ON p.id=pf.produtor_id WHERE"
                        + " pf.feira_id=? ORDER BY p.nome,p.id",
                produtores::mapProdutor,
                id);
    }

    public List<Feira> feirasDoProdutor(long id) {
        return jdbc.query(
                "SELECT f.* FROM feira f JOIN produtor_feira pf ON f.id=pf.feira_id WHERE"
                        + " pf.produtor_id=? ORDER BY f.nome,f.id",
                FeiraRepository.MAPPER,
                id);
    }

    public List<Map<String, Object>> participacoes() {
        return jdbc.queryForList(
                "SELECT pf.produtor_id,pf.feira_id,p.nome AS produtor,f.nome AS feira FROM"
                    + " produtor_feira pf JOIN produtor p ON p.id=pf.produtor_id JOIN feira f ON"
                    + " f.id=pf.feira_id ORDER BY f.nome,p.nome");
    }

    public void associar(long produtorId, long feiraId) {
        jdbc.update(
                "INSERT INTO produtor_feira(produtor_id,feira_id) VALUES (?,?)",
                produtorId,
                feiraId);
    }

    public void desassociar(long produtorId, long feiraId) {
        exigirRegistroAlterado(
                jdbc.update(
                        "DELETE FROM produtor_feira WHERE produtor_id=? AND feira_id=?",
                        produtorId,
                        feiraId));
    }
}
