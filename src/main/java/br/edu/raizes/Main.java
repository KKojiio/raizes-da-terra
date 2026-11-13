package br.edu.raizes;

import br.edu.raizes.auth.LoginService;
import br.edu.raizes.console.*;
import br.edu.raizes.service.CatalogoService;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setOut(
                new PrintStream(
                        new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
        System.setErr(
                new PrintStream(
                        new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8));
        SpringApplication.run(Main.class, args).close();
    }

    @Bean
    CommandLineRunner executar(CatalogoService service, LoginService login) {
        return args -> {
            if (java.util.Arrays.asList(args).contains("--sem-menu")) return;
            var entrada = new Entrada(new Scanner(System.in, StandardCharsets.UTF_8), System.out);
            try {
                new MenuPrincipal(entrada, service, login).executar();
            } catch (Entrada.FimDaEntrada e) {
                entrada.mostrar("Entrada encerrada. Até mais.");
            }
        };
    }
}
