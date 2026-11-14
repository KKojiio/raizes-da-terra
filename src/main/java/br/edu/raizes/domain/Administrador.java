package br.edu.raizes.domain;

public final class Administrador extends Pessoa {
    private final String email;
    private final String matricula;

    public Administrador(long id, String nome, String email, String matricula) {
        super(id, nome, null);
        this.email = email;
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public String getMatricula() {
        return matricula;
    }

    @Override
    public String obterTipoPerfil() {
        return "ADMINISTRADOR";
    }
}
