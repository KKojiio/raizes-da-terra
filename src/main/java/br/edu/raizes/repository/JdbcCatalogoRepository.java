package br.edu.raizes.repository;

import br.edu.raizes.domain.*;

import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class JdbcCatalogoRepository implements CatalogoRepository {
    private final ProdutorRepository produtores;
    private final ProdutoRepository produtos;
    private final FeiraRepository feiras;

    public JdbcCatalogoRepository(
            ProdutorRepository produtores, ProdutoRepository produtos, FeiraRepository feiras) {
        this.produtores = produtores;
        this.produtos = produtos;
        this.feiras = feiras;
    }

    @Override
    public List<Produtor> produtores() {
        return produtores.produtores();
    }

    @Override
    public Produtor produtor(long id) {
        return produtores.produtor(id);
    }

    @Override
    public long salvarProdutor(
            long id, String nome, String descricao, String telefone, String localizacao) {
        return produtores.salvarProdutor(id, nome, descricao, telefone, localizacao);
    }

    @Override
    public void excluirProdutor(long id) {
        produtores.excluirProdutor(id);
    }

    @Override
    public List<Feira> feiras() {
        return feiras.feiras();
    }

    @Override
    public Feira feira(long id) {
        return feiras.feira(id);
    }

    @Override
    public long salvarFeira(long id, String nome, String endereco, String dias, String horario) {
        return feiras.salvarFeira(id, nome, endereco, dias, horario);
    }

    @Override
    public void excluirFeira(long id) {
        feiras.excluirFeira(id);
    }

    @Override
    public List<Produto> produtos() {
        return produtos.produtos();
    }

    @Override
    public Produto produto(long id) {
        return produtos.produto(id);
    }

    @Override
    public long salvarProduto(
            long id, long produtorId, String nome, String descricao, String categoria) {
        return produtos.salvarProduto(id, produtorId, nome, descricao, categoria);
    }

    @Override
    public void excluirProduto(long id) {
        produtos.excluirProduto(id);
    }

    @Override
    public List<Produtor> produtoresDaFeira(long id) {
        return feiras.produtoresDaFeira(id);
    }

    @Override
    public List<Feira> feirasDoProdutor(long id) {
        return feiras.feirasDoProdutor(id);
    }

    @Override
    public List<Map<String, Object>> participacoes() {
        return feiras.participacoes();
    }

    @Override
    public void associar(long produtorId, long feiraId) {
        feiras.associar(produtorId, feiraId);
    }

    @Override
    public void desassociar(long produtorId, long feiraId) {
        feiras.desassociar(produtorId, feiraId);
    }
}
