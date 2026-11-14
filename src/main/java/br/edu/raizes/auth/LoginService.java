package br.edu.raizes.auth;

import br.edu.raizes.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginService {
    private static final String HASH_FICTICIO = Senhas.gerarHash("senha-ficticia-para-tempo");

    private final UsuarioRepository usuarios;

    public LoginService(
            UsuarioRepository usuarios,
            @Value("${app.admin.email:admin}") String emailInicial,
            @Value("${app.admin.password}") String senhaInicial) {
        if (senhaInicial.length() < 12)
            throw new IllegalArgumentException(
                    "ADMIN_PASSWORD precisa ter ao menos 12 caracteres.");
        this.usuarios = usuarios;
        if (usuarios.contarAdministradores() == 0)
            usuarios.criarAdministrador(
                    "Administrador", emailInicial, Senhas.gerarHash(senhaInicial), "ADM-001");
    }

    public boolean autenticar(String usuario, String tentativa) {
        var hash = usuarios.senhaHashDoAdministrador(usuario);

        boolean confere = Senhas.confere(tentativa, hash.orElse(HASH_FICTICIO));
        return hash.isPresent() && confere;
    }
}
