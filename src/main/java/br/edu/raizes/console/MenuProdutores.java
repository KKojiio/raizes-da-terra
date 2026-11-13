package br.edu.raizes.console;

import br.edu.raizes.service.CatalogoService;

public final class MenuProdutores {
    private final Entrada entrada;
    private final CatalogoService catalogo;

    public MenuProdutores(Entrada entrada, CatalogoService catalogo) {
        this.entrada = entrada;
        this.catalogo = catalogo;
    }

    public void executar() {
        while (true) {
            entrada.mostrar(
                    "\nPRODUTORES: 1 Cadastrar | 2 Listar | 3 Editar | 4 Excluir | 0 Voltar");
            String opcao = entrada.texto("Opção");
            if (opcao.equals("0")) return;
            entrada.tentar(
                    () -> {
                        switch (opcao) {
                            case "1" -> cadastrarProdutor();
                            case "2" -> listarProdutores();
                            case "3" -> editarProdutor();
                            case "4" -> excluirProdutor();
                            default -> entrada.mostrar("Opção inválida.");
                        }
                    });
        }
    }

    private void cadastrarProdutor() {

        long id =
                catalogo.salvarProdutor(
                        0,
                        entrada.texto("Nome"),
                        entrada.texto("Descrição"),
                        entrada.texto("Telefone"),
                        entrada.texto("Localização"));
        entrada.mostrar("Produtor cadastrado. ID: " + id);
    }

    private void listarProdutores() {

        var lista = catalogo.produtores();
        if (lista.isEmpty()) entrada.mostrar("Nenhum produtor cadastrado.");
        for (var produtor : lista)
            entrada.mostrar(
                    produtor.getId()
                            + " - "
                            + produtor.getNome()
                            + " | "
                            + produtor.getTelefone()
                            + " | "
                            + produtor.getLocalizacao());
    }

    private void editarProdutor() {

        var produtor = catalogo.produtor(entrada.numero("ID do produtor"));
        catalogo.salvarProdutor(
                produtor.getId(),
                entrada.campo("Nome", produtor.getNome()),
                entrada.campo("Descrição", produtor.getDescricao()),
                entrada.campo("Telefone", produtor.getTelefone()),
                entrada.campo("Localização", produtor.getLocalizacao()));
        entrada.mostrar("Produtor atualizado.");
    }

    private void excluirProdutor() {

        long id = entrada.numero("ID do produtor");
        var produtor = catalogo.produtor(id);
        if (entrada.confirmar(
                "Excluir "
                        + produtor.getNome()
                        + " remove também seus produtos e participações. As"
                        + " feiras permanecem.")) {
            catalogo.excluirProdutor(id);
            entrada.mostrar("Produtor excluído.");
        } else entrada.mostrar("Exclusão cancelada.");
    }
}
