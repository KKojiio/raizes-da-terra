# Raízes da Terra - versão de console

Catálogo de produtores, produtos e feiras da Região Metropolitana de Maringá. Java 21, Spring Boot **sem servidor web**, JDBC e MySQL 8.4. Esta versão foi criada a pedido do Ezequiel a partir da base web preparada; não é um código de console recebido dos colegas. Mantém o domínio e os dez requisitos da primeira entrega.

## Situação da integração

A branch `ezequiel-console` é destinada **somente à parte de Ezequiel e à infraestrutura compartilhada**. Ela depende dos arquivos de Junia e Karoliny para compilar e executar como projeto integrado. Não é a versão final da main. Os ZIPs individuais distribuídos à equipe incluem todos os módulos e permitem executar a base completa localmente; cada pessoa deve revisar e enviar apenas seu conjunto de arquivos e suas alterações reais.

## Abrir no VS Code

1. Extraia o ZIP e abra a pasta que contém `pom.xml` no VS Code (Arquivo > Abrir Pasta).
2. Instale JDK 21 e abra Docker Desktop. Instale **Extension Pack for Java**, recomendado pela pasta `.vscode`.
3. Abra Terminal > Novo Terminal nessa pasta. Execute:

```powershell
powershell -ExecutionPolicy Bypass -File .\iniciar.ps1
```

A primeira execução pode baixar Maven, dependências e imagem MySQL. Ao alterar código, use o comando acima: ele compila os fontes e executa. O inicializador do Maven verifica o checksum SHA-512 do download e reconhece JDK 21 instalado em `.jdks`.

Para testar apenas o executável pronto incluído nos ZIPs:

```powershell
powershell -ExecutionPolicy Bypass -File .\iniciar.ps1 -Pronto
```

**-Pronto usa o JAR empacotado e não recompila suas alterações.** Para programar, omita essa opção.

## Menu e login

O menu aparece no terminal: consultar catálogo, detalhar produtor, detalhar feira e entrar como administrador. O usuário é `admin`; a senha é o valor `ADMIN_PASSWORD` no arquivo local `.env`, criado automaticamente. Não publique `.env`. Se não houver console nativo, o programa avisa que a senha pode ficar visível ao digitar no terminal integrado.

Na administração: produtores, produtos, feiras/participações. Cada módulo contém cadastro, consulta, edição e exclusão. Produtores devem ser cadastrados antes de produtos; feiras e produtores devem existir antes de associá-los. Use os IDs mostrados nas listas. Exclusões exigem digitar `EXCLUIR` e explicam as consequências.

## Banco e encerramento

O banco desta versão usa `127.0.0.1:3308`, schema `raizes_console`, projeto Compose `aep-raizes-console` e volume próprio. Não usa o banco da versão web. Não há banco em memória. Ao selecionar 0 para sair, o Java fecha o pool; os cadastros permanecem no MySQL. `docker compose stop` pausa o banco; `iniciar.ps1` o retoma. Não use `docker compose down -v` nem apague `.env` se quiser preservar dados e acesso ao volume.

Para MySQL próprio, defina DB_URL, DB_USER, DB_PASSWORD e ADMIN_PASSWORD (ao menos 12 caracteres) e execute Maven ou o JAR sem preparar-ambiente.ps1. A variável DB_URL aceita, por exemplo, `jdbc:mysql://127.0.0.1:3306/raizes_console?connectionTimeZone=UTC`.

## F5 / depuração

Primeiro execute `powershell -ExecutionPolicy Bypass -File .\preparar-ambiente.ps1` no terminal para gerar .env e iniciar MySQL. Depois selecione a configuração “Raízes da Terra - console” e F5. O launch.json lê as variáveis de .env e usa terminal integrado; o Debug Console não aceita entrada interativa. O preLaunchTask compila as classes com Maven. O fluxo de terminal foi testado; a configuração F5 segue a documentação oficial, mas não foi validada em uma sessão aberta do VS Code.

## Testes

Execute `powershell -ExecutionPolicy Bypass -File .\testar.ps1`. A suíte usa MySQL real e reverte os dados de teste por transação. IDs podem avançar. A validação está em `docs/Validacao.md`.

## Divisão da equipe

| Integrante | Módulo | Guia |
| --- | --- | --- |
| Ezequiel | Produtores, login e infraestrutura compartilhada | [Arquivos e tarefas](docs/partes/Ezequiel.md) |
| Junia | Produtos, menu e schema.sql | [Arquivos e tarefas](docs/partes/Junia.md) |
| Karoliny | Feiras, participação, Main e MenuPrincipal | [Arquivos e tarefas](docs/partes/Karoliny.md) |

Todos os pacotes incluem a base completa para execução. Eles não são três aplicações diferentes. Antes de editar, confira seu guia e combine mudanças em arquivos compartilhados. Ainda não há contribuições reais atribuídas aos colegas. O acesso da conta Ezequielmarques261 foi confirmado em 13/11/2026 após o convite de colaborador. A publicação da parte de Ezequiel usa a branch ezequiel-console; a integração dos módulos dos colegas continua pendente.

## Arquitetura e POO

`MenuPrincipal/Menu* → CatalogoService → CatalogoRepository → repositório do módulo → MySQL`. A fachada JdbcCatalogoRepository reúne três repositórios sem misturar seus arquivos. CatalogoService coordena validações e transações; Entrada trata leitura, cancelamento e mensagens. Spring Boot cuida da configuração/injeção e conexão JDBC, sem HTTP ou navegador.

Abstração/herança: `ItemCatalogo` é abstrata; `Produtor` e `Feira` herdam dela e sobrescrevem `getResumo()`. Polimorfismo real: a consulta usa `List<ItemCatalogo>` e chama getResumo em cada objeto. Composição 1:N: cada Produto tem um Produtor; o produtor pode ter zero ou muitos produtos durante o cadastro; a FK e ON DELETE CASCADE controlam o ciclo de vida. N:N: produtor_feira, chave composta que impede repetição.

A conta admin é configuração operacional local, sem CRUD de usuários. Todos os cadastros de domínio são persistidos no MySQL. Não há pagamentos, vendas ou entregas. O catálogo é informativo; dados de demonstração devem ser identificados como fictícios.

## Requisitos preservados da primeira entrega

| ID | Requisito | Descrição |
| --- | --- | --- |
| RF01 | Cadastro de produtores | O sistema deve permitir o cadastro de produtores/feirantes, contendo nome, descrição, telefone e localização. |
| RF02 | Consulta de produtores | O sistema deve permitir a consulta dos produtores cadastrados e de suas informações. |
| RF03 | Cadastro de produtos | O sistema deve permitir o cadastro de produtos comercializados por um produtor, contendo nome, descrição e categoria. |
| RF04 | Consulta de produtos | O sistema deve permitir a consulta dos produtos associados a um produtor. |
| RF05 | Cadastro de feiras | O sistema deve permitir o cadastro de feiras, contendo nome, endereço, dias e horários de funcionamento. |
| RF06 | Consulta de feiras | O sistema deve permitir a consulta das feiras cadastradas e de suas informações. |
| RF07 | Associação produtor-feira | O sistema deve permitir associar produtores às feiras das quais participam. |
| RF08 | Associação produtor-produto | O sistema deve permitir associar produtos aos respectivos produtores. |
| RF09 | Detalhamento de feira | O sistema deve permitir consultar os produtores participantes de uma determinada feira. |
| RF10 | Contato do produtor | O sistema deve permitir visualizar os meios de contato disponibilizados pelo produtor. |

## Cronograma histórico do documento original

As datas abaixo são propostas históricas transcritas; não comprovam execução. Integrantes 1/2/3 precisam ser mapeados pela equipe. O prazo oficial da segunda entrega não foi informado.

| Data | Atividade | Responsável original |
| --- | --- | --- |
| 15/08/2026 | Levantamento e validação do problema | Todos |
| 18/08/2026 | Definição dos ODS e proposta de valor | Todos |
| 22/08/2026 | Definição e revisão dos requisitos | Todos |
| 25/08/2026 | Definição das regras de negócio | Integrante 1 |
| 29/08/2026 | Modelagem das entidades e DER | Integrante 2 |
| 03/09/2026 | Diagrama de classes | Integrante 3 |
| 04/09/2026 | Definição da arquitetura e tecnologias | Todos |
| 04/09/2026 | Criação e organização do GitHub | Integrante 1 |
| 08/09/2026 | Revisão de consistência entre requisitos e diagramas | Todos |
| 10/09/2026 | Formatação e revisão final da 1ª entrega | Todos |

## Linux e macOS

Use JDK 21, Maven e Docker Compose. Crie .env com DB_PASSWORD, MYSQL_ROOT_PASSWORD e ADMIN_PASSWORD (12+ caracteres), exporte suas variáveis, execute `docker compose up -d --wait`, `mvn verify` e `mvn spring-boot:run`. Não envie .env ao GitHub.

## Referências

- Documentação do VS Code Java: https://code.visualstudio.com/docs/java/java-debugging (envFile e integratedTerminal).
- Primeira entrega fornecida pelo aluno: catálogo de feiras/produtores/produtos; ODS 8 e 12. Esta versão preserva essa finalidade, sem alegar resultados de pesquisa ou impacto medidos.
