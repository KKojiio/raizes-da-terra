package br.edu.raizes.domain;

/** Feira livre (RF05, RN05): sempre tem endereço, dias e horário de funcionamento. */
public final class Feira implements ItemCatalogo {
    private final long id;
    private String nome;
    private String endereco;
    private String diasFuncionamento;
    private String horarioFuncionamento;

    public Feira(long id, String nome, String endereco, String dias, String horario) {
        this.id = id;
        atualizarInformacoes(nome, endereco, dias, horario);
    }

    /** Atualiza os dados da feira; a gravação no banco é feita pelo CatalogoService. */
    public void atualizarInformacoes(String nome, String endereco, String dias, String horario) {
        this.nome = nome;
        this.endereco = endereco;
        this.diasFuncionamento = dias;
        this.horarioFuncionamento = horario;
    }

    /** Detalhamento da feira (RF06, RF09): nome, endereço e funcionamento. */
    public String exibirDetalhes() {
        return nome
                + "\nEndereço: "
                + endereco
                + "\nFuncionamento: "
                + diasFuncionamento
                + ", "
                + horarioFuncionamento;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getDias() {
        return diasFuncionamento;
    }

    public String getHorario() {
        return horarioFuncionamento;
    }

    @Override
    public String getResumo() {
        return diasFuncionamento + ", " + horarioFuncionamento + " - " + endereco;
    }
}
