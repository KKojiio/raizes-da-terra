package br.edu.raizes.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class LoginService {
    private final byte[] senha;

    public LoginService(@Value("${app.admin.password}") String senha) {
        if (senha.length() < 12)
            throw new IllegalArgumentException(
                    "ADMIN_PASSWORD precisa ter ao menos 12 caracteres.");
        this.senha = senha.getBytes(StandardCharsets.UTF_8);
    }

    public boolean autenticar(String usuario, String tentativa) {
        return MessageDigest.isEqual(
                        "admin".getBytes(StandardCharsets.UTF_8),
                        usuario.getBytes(StandardCharsets.UTF_8))
                & MessageDigest.isEqual(senha, tentativa.getBytes(StandardCharsets.UTF_8));
    }
}
