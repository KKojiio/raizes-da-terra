package br.edu.raizes;

import static org.assertj.core.api.Assertions.*;

import br.edu.raizes.domain.*;
import br.edu.raizes.service.CatalogoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(args = "--sem-menu")
@Transactional
class ConsoleIntegrationTest {
    @Autowired CatalogoService s;
    @Autowired br.edu.raizes.auth.LoginService login;
    @Autowired JdbcTemplate jdbc;

    long produtor() {
        return s.salvarProdutor(0, "Produtor de teste", "Hortaliças", "44999990000", "Maringá");
    }

    long feira() {
        return s.salvarFeira(0, "Feira de teste", "Rua de teste, 10", "Sábado", "08:00 às 12:00");
    }

    @Test
    void crudProdutorComSqlReal() {
        long id = produtor();
        assertThat(s.produtor(id).getNome()).isEqualTo("Produtor de teste");
        s.salvarProdutor(id, "Novo nome", "Frutas", "4433334444", "Sarandi");
        assertThat(jdbc.queryForObject("SELECT nome FROM produtor WHERE id=?", String.class, id))
                .isEqualTo("Novo nome");
        s.excluirProdutor(id);
        assertThatThrownBy(() -> s.produtor(id)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void crudFeiraComSqlReal() {
        long id = feira();
        assertThat(s.feira(id).getDias()).isEqualTo("Sábado");
        s.salvarFeira(id, "Nova feira", "Avenida 20", "Domingo", "09:00 às 13:00");
        assertThat(s.feira(id).getNome()).isEqualTo("Nova feira");
        s.excluirFeira(id);
        assertThatThrownBy(() -> s.feira(id)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void crudProdutoEComposicao() {
        long pai = produtor();
        long id = s.salvarProduto(0, pai, "Alface", "Unidade", "Hortaliça");
        assertThat(s.produtor(pai).getProdutos()).hasSize(1);
        s.salvarProduto(id, pai, "Alface crespa", "Fresca", "Folhas");
        assertThat(s.produto(id).nome()).isEqualTo("Alface crespa");
        s.excluirProduto(id);
        assertThat(s.produtor(pai).getProdutos()).isEmpty();
    }

    @Test
    void participacoesMuitosParaMuitos() {
        long p = produtor(), f = feira(), f2 = feira();
        s.associar(p, f);
        s.associar(p, f2);
        assertThat(s.feirasDoProdutor(p)).hasSize(2);
        assertThat(s.produtoresDaFeira(f)).hasSize(1);
        s.desassociar(p, f);
        assertThat(s.produtoresDaFeira(f)).isEmpty();
        assertThat(s.feirasDoProdutor(p)).hasSize(1);
    }

    @Test
    void exclusaoDoAgregadoRemoveFilhosSemRemoverFeira() {
        long p = produtor(), f = feira();
        long filho = s.salvarProduto(0, p, "Cenoura", "Unidade", "Raízes");
        s.associar(p, f);
        s.excluirProdutor(p);
        assertThatThrownBy(() -> s.produto(filho)).isInstanceOf(IllegalArgumentException.class);
        assertThat(s.produtoresDaFeira(f)).isEmpty();
        assertThat(s.feira(f)).isNotNull();
    }

    @Test
    void exclusaoFeiraPreservaProdutorEProduto() {
        long p = produtor(),
                f = feira(),
                produto = s.salvarProduto(0, p, "Abóbora", "Unidade", "Legume");
        s.associar(p, f);
        s.excluirFeira(f);
        assertThat(s.feirasDoProdutor(p)).isEmpty();
        assertThat(s.produto(produto)).isNotNull();
    }

    @Test
    void validacoesNaoGravamDadosInvalidos() {
        assertThatThrownBy(() -> s.salvarProdutor(0, " ", "desc", "fone", "local"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> s.salvarFeira(0, "Feira", "", "Sábado", "08h"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> s.salvarProduto(0, Long.MAX_VALUE, "A", "B", "C"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> s.salvarFeira(-1, "A", "B", "C", "D"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> s.salvarProdutor(0, "x".repeat(121), "b", "c", "d"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void bancoBloqueiaDuplicidadeEOrfao() {
        long p = produtor(), f = feira();
        s.associar(p, f);
        assertThatThrownBy(() -> s.associar(p, f))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(
                        () ->
                                jdbc.update(
                                        "INSERT INTO produto(produtor_id,nome,descricao,categoria)"
                                                + " VALUES (?,?,?,?)",
                                        Long.MAX_VALUE,
                                        "a",
                                        "b",
                                        "c"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void polimorfismoEListaEncapsulada() {
        long p = produtor(), f = feira();
        ItemCatalogo a = s.produtor(p), b = s.feira(f);
        assertThat(a.getResumo()).contains("produto(s)");
        assertThat(b.getResumo()).contains("Sábado");
        assertThatThrownBy(() -> s.produtor(p).getProdutos().add(new Produto(1, p, "a", "b", "c")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    private String executarMenu(
            String dados, java.util.function.Consumer<br.edu.raizes.console.Entrada> menu) {
        var out = new java.io.ByteArrayOutputStream();
        var e =
                new br.edu.raizes.console.Entrada(
                        new java.util.Scanner(dados),
                        new java.io.PrintStream(
                                out, true, java.nio.charset.StandardCharsets.UTF_8));
        menu.accept(e);
        return out.toString(java.nio.charset.StandardCharsets.UTF_8);
    }

    @Test
    void menuProdutoresGravaNoMySql() {
        String out =
                executarMenu(
                        "1\nProdutor pelo menu\nTeste\n0000000000\nMaringá\n2\n0\n",
                        e -> new br.edu.raizes.console.MenuProdutores(e, s).executar());
        assertThat(out).contains("Produtor cadastrado.", "Produtor pelo menu");
        assertThat(s.produtores()).anyMatch(p -> p.getNome().equals("Produtor pelo menu"));
    }

    @Test
    void menuProdutosTrataNumeroInvalidoEGrava() {
        long p = produtor();
        String out =
                executarMenu(
                        "1\nabc\n" + p + "\nProduto pelo menu\nDescrição\nCategoria\n2\n0\n",
                        e -> new br.edu.raizes.console.MenuProdutos(e, s).executar());
        assertThat(out)
                .contains("Digite um número inteiro", "Produto cadastrado.", "Produto pelo menu");
        assertThat(s.produtor(p).getProdutos()).hasSize(1);
    }

    @Test
    void menuFeirasAssociaConsultaERemove() {
        long p = produtor(), f = feira();
        String out =
                executarMenu(
                        "5\n" + p + "\n" + f + "\n7\n" + f + "\n6\n" + p + "\n" + f + "\n0\n",
                        e -> new br.edu.raizes.console.MenuFeiras(e, s).executar());
        assertThat(out)
                .contains(
                        "Participação cadastrada.", "Produtor de teste", "Participação removida.");
        assertThat(s.produtoresDaFeira(f)).isEmpty();
    }

    @Test
    void loginIncorretoNaoAbreAdministracao() {
        String out =
                executarMenu(
                        "4\nadmin\nerrada\nadmin\nerrada\nadmin\nerrada\n0\n",
                        e -> new br.edu.raizes.console.MenuPrincipal(e, s, login).executar());
        assertThat(out).contains("Tentativa 3 de 3").doesNotContain("ADMINISTRAÇÃO:");
    }

    @Test
    void loginCorretoESaidaDaConta() {
        String out =
                executarMenu(
                        "4\nadmin\n" + System.getenv("ADMIN_PASSWORD") + "\n0\n0\n",
                        e -> new br.edu.raizes.console.MenuPrincipal(e, s, login).executar());
        assertThat(out).contains("Login realizado.", "ADMINISTRAÇÃO:", "Sessão encerrada.");
    }

    @Test
    void cancelamentoPreservaProdutor() {
        long p = produtor();
        String out =
                executarMenu(
                        "4\n" + p + "\nnão\n0\n",
                        e -> new br.edu.raizes.console.MenuProdutores(e, s).executar());
        assertThat(out).contains("Exclusão cancelada.");
        assertThat(s.produtor(p)).isNotNull();
    }

    @Test
    void entradaEncerradaNaoFicaEmLoop() {
        assertThatThrownBy(
                        () ->
                                executarMenu(
                                        "",
                                        e ->
                                                new br.edu.raizes.console.MenuPrincipal(e, s, login)
                                                        .executar()))
                .isInstanceOf(br.edu.raizes.console.Entrada.FimDaEntrada.class);
    }

    @Test
    void diaInvalidoNaoGravaFeira() {
        int antes = jdbc.queryForObject("SELECT COUNT(*) FROM feira", Integer.class);
        assertThatThrownBy(() -> s.salvarFeira(0, "Inválida", "Rua 1", "45", "08:00 às 12:00"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM feira", Integer.class))
                .isEqualTo(antes);
    }

    @Test
    void horarioInvalidoNaoAlteraFeiraExistente() {
        long id = feira();
        assertThatThrownBy(() -> s.salvarFeira(id, "Alterada", "Rua 2", "Domingo", "4:50"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(s.feira(id).getNome()).isEqualTo("Feira de teste");
    }
}
