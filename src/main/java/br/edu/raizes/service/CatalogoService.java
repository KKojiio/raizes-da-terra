package br.edu.raizes.service;

import br.edu.raizes.domain.*;
import br.edu.raizes.repository.CatalogoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class CatalogoService {
    private final CatalogoRepository repositorio;

    public CatalogoService(CatalogoRepository repositorio) {
        this.repositorio = repositorio;
    }

    private String validarTexto(String valor, String campo, int limite) {
        if (valor == null || valor.isBlank())
            throw new IllegalArgumentException("Preencha " + campo + ".");
        String textoValidado = valor.strip();
        if (textoValidado.length() > limite)
            throw new IllegalArgumentException(campo + ": limite de " + limite + " caracteres.");
        return textoValidado;
    }

    private void validarIdentificador(long id) {
        if (id < 0) throw new IllegalArgumentException("Identificador inválido.");
    }

    public List<Produtor> produtores() {
        return repositorio.produtores();
    }

    public List<Feira> feiras() {
        return repositorio.feiras();
    }

    public List<Produto> produtos() {
        return repositorio.produtos();
    }

    public Produtor produtor(long id) {
        return repositorio.produtor(id);
    }

    public Feira feira(long id) {
        return repositorio.feira(id);
    }

    public Produto produto(long id) {
        return repositorio.produto(id);
    }

    public List<Produtor> produtoresDaFeira(long id) {
        repositorio.feira(id);
        return repositorio.produtoresDaFeira(id);
    }

    public List<Feira> feirasDoProdutor(long id) {
        repositorio.produtor(id);
        return repositorio.feirasDoProdutor(id);
    }

    public List<Map<String, Object>> participacoes() {
        return repositorio.participacoes();
    }

    public List<ItemCatalogo> destaques() {
        List<ItemCatalogo> itens = new ArrayList<>();
        itens.addAll(feiras());
        itens.addAll(produtores());
        return List.copyOf(itens);
    }

    @Transactional
    public long salvarProdutor(
            long id, String nome, String descricao, String telefone, String localizacao) {
        validarIdentificador(id);
        return repositorio.salvarProdutor(
                id,
                validarTexto(nome, "nome", 120),
                validarTexto(descricao, "descrição", 1000),
                validarTexto(telefone, "telefone", 25),
                validarTexto(localizacao, "localização", 180));
    }

    @Transactional
    public long salvarFeira(long id, String nome, String endereco, String dias, String horario) {
        validarIdentificador(id);
        return repositorio.salvarFeira(
                id,
                validarTexto(nome, "nome", 120),
                validarTexto(endereco, "endereço", 200),
                validarTexto(dias, "dias", 120),
                validarTexto(horario, "horário", 120));
    }

    @Transactional
    public long salvarProduto(
            long id, long produtorId, String nome, String descricao, String categoria) {
        validarIdentificador(id);
        repositorio.produtor(produtorId);
        return repositorio.salvarProduto(
                id,
                produtorId,
                validarTexto(nome, "nome", 120),
                validarTexto(descricao, "descrição", 1000),
                validarTexto(categoria, "categoria", 80));
    }

    @Transactional
    public void excluirProdutor(long id) {
        repositorio.excluirProdutor(id);
    }

    @Transactional
    public void excluirFeira(long id) {
        repositorio.excluirFeira(id);
    }

    @Transactional
    public void excluirProduto(long id) {
        repositorio.excluirProduto(id);
    }

    @Transactional
    public void associar(long produtorId, long feiraId) {
        repositorio.produtor(produtorId);
        repositorio.feira(feiraId);
        repositorio.associar(produtorId, feiraId);
    }

    @Transactional
    public void desassociar(long produtorId, long feiraId) {
        repositorio.desassociar(produtorId, feiraId);
    }
}
