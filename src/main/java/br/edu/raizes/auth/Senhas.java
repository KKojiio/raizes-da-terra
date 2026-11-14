package br.edu.raizes.auth;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class Senhas {
    private static final int ITERACOES = 120_000;
    private static final int BITS = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private Senhas() {}

    static String gerarHash(String senha) {
        byte[] sal = new byte[16];
        ALEATORIO.nextBytes(sal);
        return "pbkdf2$"
                + ITERACOES
                + "$"
                + Base64.getEncoder().encodeToString(sal)
                + "$"
                + Base64.getEncoder().encodeToString(derivar(senha, sal, ITERACOES));
    }

    static boolean confere(String senha, String hashGravado) {
        String[] partes = hashGravado.split("\\$");
        if (partes.length != 4 || !partes[0].equals("pbkdf2")) return false;
        byte[] sal = Base64.getDecoder().decode(partes[2]);
        byte[] esperado = Base64.getDecoder().decode(partes[3]);
        byte[] calculado = derivar(senha, sal, Integer.parseInt(partes[1]));
        return MessageDigest.isEqual(esperado, calculado);
    }

    private static byte[] derivar(String senha, byte[] sal, int iteracoes) {
        var especificacao = new PBEKeySpec(senha.toCharArray(), sal, iteracoes, BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(especificacao)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 indisponível nesta JVM.", e);
        } finally {
            especificacao.clearPassword();
        }
    }
}
