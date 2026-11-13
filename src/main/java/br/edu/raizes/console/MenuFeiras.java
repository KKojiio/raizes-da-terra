package br.edu.raizes.console;

import br.edu.raizes.service.CatalogoService;

public final class MenuFeiras {
    private final Entrada entrada;
    private final CatalogoService catalogo;

    public MenuFeiras(Entrada entrada, CatalogoService catalogo) {
        this.entrada = entrada;
        this.catalogo = catalogo;
    }

    public void executar() {
        while (true) {
            entrada.mostrar(
                    "\n"
                        + "FEIRAS: 1 Cadastrar | 2 Listar | 3 Editar | 4 Excluir | 5 Associar"
                        + " produtor | 6 Remover participação | 7 Ver participantes | 0 Voltar");
            String opcao = entrada.texto("Opção");
            if (opcao.equals("0")) return;
            entrada.tentar(
                    () -> {
                        switch (opcao) {
                            case "1" -> cadastrarFeira();
                            case "2" -> listarFeiras();
                            case "3" -> editarFeira();
                            case "4" -> excluirFeira();
                            case "5" -> associarProdutor();
                            case "6" -> removerParticipacao();
                            case "7" -> listarParticipantes();
                            default -> entrada.mostrar("Opção inválida.");
                        }
                    });
        }
    }

    private void cadastrarFeira() {

        long id =
                catalogo.salvarFeira(
                        0,
                        entrada.texto("Nome"),
                        entrada.texto("Endereço"),
                        entrada.texto("Dias (ex.: sábado ou terça e quinta)"),
                        entrada.texto("Horário (ex.: 08:00 às 12:00)"));
        entrada.mostrar("Feira cadastrada. ID: " + id);
    }

    private void listarFeiras() {

        var lista = catalogo.feiras();
        if (lista.isEmpty()) entrada.mostrar("Nenhuma feira cadastrada.");
        for (var feira : lista)
            entrada.mostrar(feira.getId() + " - " + feira.getNome() + " | " + feira.getResumo());
    }

    private void editarFeira() {

        var feira = catalogo.feira(entrada.numero("ID da feira"));
        catalogo.salvarFeira(
                feira.getId(),
                entrada.campo("Nome", feira.getNome()),
                entrada.campo("Endereço", feira.getEndereco()),
                entrada.campo("Dias", feira.getDias()),
                entrada.campo("Horário", feira.getHorario()));
        entrada.mostrar("Feira atualizada.");
    }

    private void excluirFeira() {

        long id = entrada.numero("ID da feira");
        var feira = catalogo.feira(id);
        if (entrada.confirmar(
                "Excluir "
                        + feira.getNome()
                        + " remove as participações, preservando produtores"
                        + " e produtos.")) {
            catalogo.excluirFeira(id);
            entrada.mostrar("Feira excluída.");
        } else entrada.mostrar("Exclusão cancelada.");
    }

    private void associarProdutor() {

        long produtor = entrada.numero("ID do produtor"), feira = entrada.numero("ID da feira");
        catalogo.associar(produtor, feira);
        entrada.mostrar("Participação cadastrada.");
    }

    private void removerParticipacao() {

        long produtor = entrada.numero("ID do produtor"), feira = entrada.numero("ID da feira");
        catalogo.desassociar(produtor, feira);
        entrada.mostrar("Participação removida.");
    }

    private void listarParticipantes() {

        var lista = catalogo.produtoresDaFeira(entrada.numero("ID da feira"));
        if (lista.isEmpty()) entrada.mostrar("Nenhum participante nesta feira.");
        for (var produtor : lista)
            entrada.mostrar(
                    produtor.getId()
                            + " - "
                            + produtor.getNome()
                            + " | contato: "
                            + produtor.getTelefone());
    }
}
