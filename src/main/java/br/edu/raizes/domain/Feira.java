package br.edu.raizes.domain;

public final class Feira extends ItemCatalogo {
    private final String endereco;
    private final String dias;
    private final String horario;

    public Feira(long id, String nome, String endereco, String dias, String horario) {
        super(id, nome);
        this.endereco = endereco;
        this.dias = dias;
        this.horario = horario;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getDias() {
        return dias;
    }

    public String getHorario() {
        return horario;
    }

    @Override
    public String getResumo() {
        return dias + ", " + horario + " - " + endereco;
    }
}
