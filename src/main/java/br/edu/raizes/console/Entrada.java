package br.edu.raizes.console;
import java.io.PrintStream;
import java.util.Scanner;
import org.springframework.dao.*;

public final class Entrada {
    private final Scanner scanner;
    private final PrintStream out;
    public Entrada(Scanner scanner,PrintStream out) { this.scanner=scanner;this.out=out; }
    public void mostrar(String texto) { out.println(texto); }
    public String texto(String rotulo) {
        out.print(rotulo+": ");
        if(!scanner.hasNextLine()) throw new FimDaEntrada();
        return scanner.nextLine().strip();
    }
    public long numero(String rotulo) {
        while(true) {
            try { return Long.parseLong(texto(rotulo)); }
            catch(NumberFormatException e) { mostrar("Digite um número inteiro, como 1 ou 2."); }
        }
    }
    public String campo(String rotulo,String atual) {
        String novo=texto(rotulo+" [Enter mantém: "+atual+"]");return novo.isBlank()?atual:novo;
    }
    public boolean confirmar(String aviso) { mostrar(aviso);return texto("Digite EXCLUIR para confirmar").equals("EXCLUIR"); }
    public void tentar(Runnable acao) {
        try { acao.run(); }
        catch(FimDaEntrada e) { throw e; }
        catch(IllegalArgumentException e) { mostrar("Atenção: "+e.getMessage()); }
        catch(DataIntegrityViolationException e) { mostrar("Participação duplicada ou dados relacionados indisponíveis. Confira os cadastros."); }
        catch(DataAccessException e) { mostrar("Não foi possível consultar ou gravar no banco. Confira o MySQL e tente novamente."); }
    }
    public static final class FimDaEntrada extends RuntimeException {}
}
