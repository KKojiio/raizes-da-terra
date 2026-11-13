package br.edu.raizes.console;
import br.edu.raizes.service.CatalogoService;

public final class MenuProdutores {
    private final Entrada e; private final CatalogoService s;
    public MenuProdutores(Entrada e,CatalogoService s) { this.e=e;this.s=s; }
    public void executar() {
        while(true) {
            e.mostrar("\nPRODUTORES: 1 Cadastrar | 2 Listar | 3 Editar | 4 Excluir | 0 Voltar");
            String op=e.texto("Opção"); if(op.equals("0")) return;
            e.tentar(() -> {
                switch(op) {
                    case "1" -> { long id=s.salvarProdutor(0,e.texto("Nome"),e.texto("Descrição"),e.texto("Telefone"),e.texto("Localização"));e.mostrar("Produtor cadastrado. ID: "+id); }
                    case "2" -> { var lista=s.produtores();if(lista.isEmpty()) e.mostrar("Nenhum produtor cadastrado.");for(var p:lista)e.mostrar(p.getId()+" - "+p.getNome()+" | "+p.getTelefone()+" | "+p.getLocalizacao()); }
                    case "3" -> { var p=s.produtor(e.numero("ID do produtor"));s.salvarProdutor(p.getId(),e.campo("Nome",p.getNome()),e.campo("Descrição",p.getDescricao()),e.campo("Telefone",p.getTelefone()),e.campo("Localização",p.getLocalizacao()));e.mostrar("Produtor atualizado."); }
                    case "4" -> { long id=e.numero("ID do produtor");var p=s.produtor(id);if(e.confirmar("Excluir "+p.getNome()+" remove também seus produtos e participações. As feiras permanecem.")){s.excluirProdutor(id);e.mostrar("Produtor excluído.");}else e.mostrar("Exclusão cancelada."); }
                    default -> e.mostrar("Opção inválida.");
                }
            });
        }
    }
}
