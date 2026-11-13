package br.edu.raizes.repository;

import br.edu.raizes.domain.*;
import java.util.List;
import java.util.Map;

public interface CatalogoRepository {
    List<Produtor> produtores();
    Produtor produtor(long id);
    long salvarProdutor(long id, String nome, String descricao, String telefone, String localizacao);
    void excluirProdutor(long id);
    List<Feira> feiras();
    Feira feira(long id);
    long salvarFeira(long id, String nome, String endereco, String dias, String horario);
    void excluirFeira(long id);
    List<Produto> produtos();
    Produto produto(long id);
    long salvarProduto(long id, long produtorId, String nome, String descricao, String categoria);
    void excluirProduto(long id);
    List<Produtor> produtoresDaFeira(long feiraId);
    List<Feira> feirasDoProdutor(long produtorId);
    List<Map<String,Object>> participacoes();
    void associar(long produtorId, long feiraId);
    void desassociar(long produtorId, long feiraId);
}
