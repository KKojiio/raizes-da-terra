package br.edu.raizes.repository;
import br.edu.raizes.domain.*;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutorRepository extends JdbcBase {
    public ProdutorRepository(JdbcTemplate jdbc) { super(jdbc); }
    Produtor mapProdutor(ResultSet r,int n) throws SQLException {
        long id=r.getLong("id");
        return new Produtor(id,r.getString("nome"),r.getString("descricao"),r.getString("telefone"),r.getString("localizacao"),jdbc.query("SELECT * FROM produto WHERE produtor_id=? ORDER BY nome,id",ProdutoRepository.MAPPER,id));
    }
    public List<Produtor> produtores() { return jdbc.query("SELECT * FROM produtor ORDER BY nome,id", this::mapProdutor); }
    public Produtor produtor(long id) { return jdbc.query("SELECT * FROM produtor WHERE id=?", this::mapProdutor, id).stream().findFirst().orElseThrow(() -> new IllegalArgumentException("Produtor não encontrado.")); }
    public long salvarProdutor(long id, String nome, String descricao, String telefone, String localizacao) { if(id == 0) return inserir("INSERT INTO produtor(nome,descricao,telefone,localizacao) VALUES (?,?,?,?)", nome,descricao,telefone,localizacao);
        mudou(jdbc.update("UPDATE produtor SET nome=?,descricao=?,telefone=?,localizacao=? WHERE id=?", nome,descricao,telefone,localizacao,id)); return id; }
    public void excluirProdutor(long id) { mudou(jdbc.update("DELETE FROM produtor WHERE id=?", id)); }
}
