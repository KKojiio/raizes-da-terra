package br.edu.raizes.console;

import br.edu.raizes.auth.LoginService;
import br.edu.raizes.service.CatalogoService;

public final class MenuPrincipal {
    private final Entrada entrada;
    private final CatalogoService catalogo;
    private final LoginService login;

    public MenuPrincipal(Entrada entrada, CatalogoService catalogo, LoginService login) {
        this.entrada = entrada;
        this.catalogo = catalogo;
        this.login = login;
    }

    public void executar() {
        entrada.mostrar("RAÍZES DA TERRA - catálogo de feiras e produtores locais");
        while (true) {
            entrada.mostrar(
                    "\n"
                        + "1 Consultar catálogo | 2 Detalhar produtor | 3 Detalhar feira | 4 Entrar"
                        + " como administrador | 0 Sair");
            String opcao = entrada.texto("Opção");
            if (opcao.equals("0")) {
                entrada.mostrar("Até mais.");
                return;
            }
            entrada.tentar(
                    () -> {
                        switch (opcao) {
                            case "1" -> consultarCatalogo();
                            case "2" -> detalharProdutor();
                            case "3" -> detalharFeira();
                            case "4" -> entrarNaAdministracao();
                            default -> entrada.mostrar("Opção inválida.");
                        }
                    });
        }
    }

    private boolean autenticar() {
        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            String usuario = entrada.texto("Usuário");
            String senha;
            if (System.console() != null) {
                char[] caracteres = System.console().readPassword("Senha: ");
                if (caracteres == null) throw new Entrada.FimDaEntrada();
                senha = new String(caracteres);
                java.util.Arrays.fill(caracteres, '\0');
            } else senha = entrada.texto("Senha (entrada pode ficar visível neste terminal)");
            if (login.autenticar(usuario, senha)) {
                entrada.mostrar("Login realizado.");
                return true;
            }
            entrada.mostrar("Usuário ou senha inválidos. Tentativa " + tentativa + " de 3.");
        }
        entrada.mostrar("Voltando ao catálogo. Nenhum cadastro foi alterado.");
        return false;
    }

    private void administrar() {
        while (true) {
            entrada.mostrar(
                    "\n"
                        + "ADMINISTRAÇÃO: 1 Produtores | 2 Produtos | 3 Feiras e participação | 0"
                        + " Sair da conta");
            switch (entrada.texto("Opção")) {
                case "1" -> new MenuProdutores(entrada, catalogo).executar();
                case "2" -> new MenuProdutos(entrada, catalogo).executar();
                case "3" -> new MenuFeiras(entrada, catalogo).executar();
                case "0" -> {
                    entrada.mostrar("Sessão encerrada.");
                    return;
                }
                default -> entrada.mostrar("Opção inválida.");
            }
        }
    }

    private void consultarCatalogo() {

        var itens = catalogo.destaques();
        if (itens.isEmpty()) entrada.mostrar("Catálogo vazio.");
        for (var item : itens)
            entrada.mostrar(item.getId() + " - " + item.getNome() + " | " + item.getResumo());
    }

    private void detalharProdutor() {

        var produtor = catalogo.produtor(entrada.numero("ID do produtor"));
        entrada.mostrar(
                produtor.getNome()
                        + " | "
                        + produtor.getDescricao()
                        + " | contato: "
                        + produtor.getTelefone()
                        + " | "
                        + produtor.getLocalizacao());
        for (var produto : produtor.getProdutos())
            entrada.mostrar(
                    "Produto "
                            + produto.id()
                            + ": "
                            + produto.nome()
                            + " | "
                            + produto.descricao()
                            + " | "
                            + produto.categoria());
        for (var feira : catalogo.feirasDoProdutor(produtor.getId()))
            entrada.mostrar("Feira: " + feira.getNome() + " | " + feira.getResumo());
    }

    private void detalharFeira() {

        long id = entrada.numero("ID da feira");
        var feira = catalogo.feira(id);
        entrada.mostrar(feira.getNome() + " | " + feira.getResumo());
        for (var produtor : catalogo.produtoresDaFeira(id))
            entrada.mostrar(
                    "Produtor: " + produtor.getNome() + " | contato: " + produtor.getTelefone());
    }

    private void entrarNaAdministracao() {

        if (autenticar()) administrar();
    }
}
