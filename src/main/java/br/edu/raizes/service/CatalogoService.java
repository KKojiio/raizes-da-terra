package br.edu.raizes.service;

import br.edu.raizes.domain.*;
import br.edu.raizes.repository.CatalogoRepository;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
public class CatalogoService {
    private final CatalogoRepository repo;
    public CatalogoService(CatalogoRepository repo) { this.repo = repo; }
    private String texto(String value,String campo,int max) {
        if(value == null || value.isBlank()) throw new IllegalArgumentException("Preencha " + campo + ".");
        String result = value.strip();
        if(result.length() > max) throw new IllegalArgumentException(campo + ": limite de " + max + " caracteres.");
        return result;
    }
    private void id(long id) { if(id < 0) throw new IllegalArgumentException("Identificador inválido."); }
    public List<Produtor> produtores() { return repo.produtores(); }
    public List<Feira> feiras() { return repo.feiras(); }
    public List<Produto> produtos() { return repo.produtos(); }
    public Produtor produtor(long id) { return repo.produtor(id); }
    public Feira feira(long id) { return repo.feira(id); }
    public Produto produto(long id) { return repo.produto(id); }
    public List<Produtor> produtoresDaFeira(long id) { repo.feira(id); return repo.produtoresDaFeira(id); }
    public List<Feira> feirasDoProdutor(long id) { repo.produtor(id); return repo.feirasDoProdutor(id); }
    public List<Map<String,Object>> participacoes() { return repo.participacoes(); }
    public List<ItemCatalogo> destaques() {
        List<ItemCatalogo> itens = new ArrayList<>(); itens.addAll(feiras()); itens.addAll(produtores());
        return List.copyOf(itens); // A tela chama getResumo/getCaminho sem conhecer o subtipo.
    }
    @Transactional public long salvarProdutor(long id,String nome,String descricao,String telefone,String localizacao) {
        id(id); return repo.salvarProdutor(id,texto(nome,"nome",120),texto(descricao,"descrição",1000),texto(telefone,"telefone",25),texto(localizacao,"localização",180));
    }
    @Transactional public long salvarFeira(long id,String nome,String endereco,String dias,String horario) {
        id(id); return repo.salvarFeira(id,texto(nome,"nome",120),texto(endereco,"endereço",200),texto(dias,"dias",120),texto(horario,"horário",120));
    }
    @Transactional public long salvarProduto(long id,long produtorId,String nome,String descricao,String categoria) {
        id(id); repo.produtor(produtorId);
        return repo.salvarProduto(id,produtorId,texto(nome,"nome",120),texto(descricao,"descrição",1000),texto(categoria,"categoria",80));
    }
    @Transactional public void excluirProdutor(long id) { repo.excluirProdutor(id); }
    @Transactional public void excluirFeira(long id) { repo.excluirFeira(id); }
    @Transactional public void excluirProduto(long id) { repo.excluirProduto(id); }
    @Transactional public void associar(long produtorId,long feiraId) { repo.produtor(produtorId); repo.feira(feiraId); repo.associar(produtorId,feiraId); }
    @Transactional public void desassociar(long produtorId,long feiraId) { repo.desassociar(produtorId,feiraId); }
}
