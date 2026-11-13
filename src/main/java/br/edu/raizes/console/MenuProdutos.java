package br.edu.raizes.console;

import br.edu.raizes.service.CatalogoService;

public final class MenuProdutos {
    private final Entrada entrada;
    private final CatalogoService catalogo;

    public MenuProdutos(Entrada entrada, CatalogoService catalogo) {
        this.entrada = entrada;
        this.catalogo = catalogo;
    }

    public void executar() {
        while (true) {
            entrada.mostrar("\nPRODUTOS: 1 Cadastrar | 2 Listar | 3 Editar | 4 Excluir | 0 Voltar");
            String opcao = entrada.texto("Opção");
            if (opcao.equals("0")) return;
            entrada.tentar(
                    () -> {
                        switch (opcao) {
                            case "1" -> cadastrarProduto();
                            case "2" -> listarProdutos();
                            case "3" -> editarProduto();
                            case "4" -> excluirProduto();
                            default -> entrada.mostrar("Opção inválida.");
                        }
                    });
        }
    }

    private void cadastrarProduto() {

        long produto = entrada.numero("ID do produtor proprietário");
        long id =
                catalogo.salvarProduto(
                        0,
                        produto,
                        entrada.texto("Nome"),
                        entrada.texto("Descrição"),
                        entrada.texto("Categoria"));
        entrada.mostrar("Produto cadastrado. ID: " + id);
    }

    private void listarProdutos() {

        var lista = catalogo.produtos();
        if (lista.isEmpty()) entrada.mostrar("Nenhum produto cadastrado.");
        for (var produto : lista)
            entrada.mostrar(
                    produto.id()
                            + " - "
                            + produto.nome()
                            + " | produtor "
                            + produto.produtorId()
                            + " | "
                            + produto.categoria());
    }

    private void editarProduto() {

        var produto = catalogo.produto(entrada.numero("ID do produto"));
        catalogo.salvarProduto(
                produto.id(),
                produto.produtorId(),
                entrada.campo("Nome", produto.nome()),
                entrada.campo("Descrição", produto.descricao()),
                entrada.campo("Categoria", produto.categoria()));
        entrada.mostrar("Produto atualizado; proprietário preservado.");
    }

    private void excluirProduto() {

        long id = entrada.numero("ID do produto");
        var produto = catalogo.produto(id);
        if (entrada.confirmar("Excluir produto " + produto.nome() + "?")) {
            catalogo.excluirProduto(id);
            entrada.mostrar("Produto excluído.");
        } else entrada.mostrar("Exclusão cancelada.");
    }
}
