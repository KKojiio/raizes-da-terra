# Raízes da Terra

Catálogo de feiras livres, produtores e produtos da Região Metropolitana de Maringá. A plataforma ajuda consumidores a descobrir a produção local e dá visibilidade aos produtores, sem intermediar vendas. Projeto acadêmico (AEP, 4º semestre) de Engenharia de Software — Unicesumar. Alinhado aos **ODS 8** (trabalho decente e crescimento econômico) e **ODS 12** (consumo e produção responsáveis).

**Integrantes:** Ezequiel Santos Silva · Junia Bernardineli Soares de Resende · Karoliny Beatriz Oliveira do Nascimento

**Tecnologias:** Java 21 · Spring Boot (sem servidor web, só injeção de dependências e JDBC) · MySQL 8.4 · Maven · Docker Compose · aplicação de console.

## Como executar

### Windows (recomendado)

**Requisitos:** [JDK 21](https://adoptium.net/) e [Docker Desktop](https://www.docker.com/products/docker-desktop/) aberto. O Maven não precisa estar instalado: o script baixa a versão 3.9.11 e confere o checksum SHA-512.

Na pasta do projeto (a que contém o `pom.xml`), execute no PowerShell:

```powershell
powershell -ExecutionPolicy Bypass -File .\iniciar.ps1
```

Na primeira execução o script:

1. cria o arquivo local `.env` com senhas aleatórias (`DB_PASSWORD`, `MYSQL_ROOT_PASSWORD` e `ADMIN_PASSWORD`). **Não envie o `.env` ao GitHub**;
2. sobe o MySQL no Docker (porta `127.0.0.1:3308`, banco `raizes_console`);
3. cria as tabelas a partir de [`database/schema.sql`](database/schema.sql), compila e abre o menu no terminal.

Os dados ficam salvos no volume do Docker entre execuções. Se já houver outra cópia deste projeto usando o mesmo banco no computador, reutilize o arquivo `.env` da cópia que criou o volume; gerar senhas diferentes para o mesmo volume causa falha de autenticação. Não publique esse arquivo. Para pausar o banco: `docker compose stop`.

### Linux / macOS (ou MySQL próprio)

Com JDK 21, Maven e Docker Compose instalados:

```bash
cat > .env <<EOF
DB_PASSWORD=troque-esta-senha
MYSQL_ROOT_PASSWORD=troque-esta-tambem
ADMIN_PASSWORD=senha-admin-com-12+
EOF
set -a; . ./.env; set +a
docker compose up -d --wait
mvn spring-boot:run
```

Para usar um MySQL já instalado, defina `DB_URL` (ex.: `jdbc:mysql://127.0.0.1:3306/raizes_console?connectionTimeZone=UTC`), `DB_USER`, `DB_PASSWORD` e `ADMIN_PASSWORD` (mínimo de 12 caracteres) e rode `mvn spring-boot:run`.

### Testes

```powershell
powershell -ExecutionPolicy Bypass -File .\testar.ps1
```

São 42 casos de teste: 18 de integração contra o **MySQL real**, com transações desfeitas ao final, 21 de validação de dias e horários e 3 da classe `Feira`. Não há banco em memória.

## Usando o sistema

Menu inicial (consulta pública, sem login):

| Opção | O que faz | Requisitos |
|---|---|---|
| 1 Consultar catálogo | Lista feiras e produtores com um resumo de cada | RF02, RF06 |
| 2 Detalhar produtor | Contato, localização, produtos e feiras de um produtor | RF04, RF10 |
| 3 Detalhar feira | Endereço, dias, horário e produtores participantes | RF06, RF09 |
| 4 Entrar como administrador | Usuário `admin`; senha = `ADMIN_PASSWORD` do arquivo `.env` (3 tentativas) | — |

Administração (cadastrar, listar, editar e excluir):

| Menu | Operações | Requisitos |
|---|---|---|
| Produtores | CRUD completo | RF01, RF02 |
| Produtos | CRUD completo; todo produto pertence a um produtor | RF03, RF04, RF08 |
| Feiras e participação | CRUD de feiras, associar/remover produtor e listar participantes | RF05, RF06, RF07, RF09 |

Dias aceitam nomes como `sábado`, listas como `terça e quinta` e intervalos como `segunda a sexta`. O horário exige início e fim no mesmo dia, como `08:00 às 12:00` ou `8h às 12h`. Valores inválidos são rejeitados antes de gravar.

Ordem sugerida: cadastre um produtor → um produto dele → uma feira → associe o produtor à feira. Os IDs aparecem ao cadastrar e listar. Exclusões pedem a palavra `EXCLUIR` para confirmar.

## Estrutura

```
├── src/main/java/br/edu/raizes/
│   ├── Main.java              ponto de entrada
│   ├── console/               menus (camada de controle)
│   ├── service/               regras de negócio e transações
│   ├── repository/            acesso ao MySQL via JDBC
│   ├── domain/                classes do domínio
│   └── auth/                  login do administrador
├── src/test/                  testes de integração
├── database/schema.sql        script de criação das tabelas
├── docs/                      PDF da 1ª entrega e diagramas (PlantUML)
├── compose.yaml               MySQL no Docker
└── *.ps1                      scripts de configuração, execução e testes
```

**Arquitetura em camadas:** `Menu* (controle) → CatalogoService (regras) → CatalogoRepository (interface) → ProdutorRepository / ProdutoRepository / FeiraRepository → MySQL`. Os diagramas do modelo implementado estão em [`docs/diagramas`](docs/diagramas); os diagramas planejados estão no [documento da 1ª entrega](docs/AEP_1a_entrega.pdf). As diferenças entre os dois estão explicadas em [Evolução do modelo](#evolução-do-modelo-1ª--2ª-entrega).

## Pilares de POO

| Pilar | Onde |
|---|---|
| Classe abstrata | `JdbcBase`, base dos repositórios (inserção e verificação de registros) |
| Interface | `ItemCatalogo`, implementada por `Produtor` e `Feira`; `CatalogoRepository`, implementada por `JdbcCatalogoRepository` |
| Herança | `ProdutorRepository`, `ProdutoRepository` e `FeiraRepository` estendem `JdbcBase` |
| Polimorfismo (`@Override`) | `Produtor` e `Feira` implementam `getResumo()` cada um do seu jeito; o catálogo percorre uma `List<ItemCatalogo>` e chama `getResumo()` em cada item |
| Composição 1:N | Um `Produtor` possui uma lista de `Produto`; no banco, `produto.produtor_id` com `ON DELETE CASCADE` |
| Encapsulamento | Atributos `private final`, lista de produtos imutável |

## Evolução do modelo (1ª → 2ª entrega)

O escopo planejado na 1ª entrega foi mantido por inteiro: **os requisitos RF01–RF10 e as regras RN01–RN08 estão implementados**, com Java, MySQL e arquitetura em camadas, como previsto. Durante a implementação, o modelo de classes e o DER foram simplificados para o tamanho do MVP. As mudanças e seus motivos:

| Planejado (1ª entrega) | Implementado (2ª entrega) | Motivo |
|---|---|---|
| Classe abstrata `Pessoa`, especializada em `Administrador` e `Produtor`, com polimorfismo em `obterTipoPerfil()` | Interface `ItemCatalogo`, implementada por `Produtor` e `Feira`, com polimorfismo em `getResumo()` (a classe `Pessoa` do diagrama está sendo incorporada) | Nenhum requisito pede cadastro de usuários: o administrador é um operador único, não uma entidade do catálogo. Já produtores e feiras são justamente o que o consumidor consulta, e o catálogo os trata juntos numa `List<ItemCatalogo>`, chamando `getResumo()` em cada um. Assim o polimorfismo é usado de fato na consulta, e não só declarado. O próprio documento da 1ª entrega apresentou `Pessoa` como "uma possibilidade". |
| Tabelas `usuario` e `administrador` | Login do administrador por configuração (`ADMIN_PASSWORD` no `.env`) | Mesmo motivo: não há requisito de gestão de usuários. O sistema evita guardar senhas no banco sem necessidade. |
| Tabelas `categoria`, `contato_produtor` e `horario_feira` | Campos `categoria` (produto), `telefone` (produtor), `dias` e `horario` (feira) | O RF03 pede a categoria, o RF01/RF10 pedem o telefone como contato e o RF05 pede dias e horários. Os campos atendem a esses requisitos com menos tabelas; dias e horários são validados antes de gravar (`FuncionamentoFeira`). |
| Endereço detalhado e coordenadas da feira, preço de referência, unidade e disponibilidade do produto, banca e data de entrada na feira | Não implementados | Não constam nos requisitos. Pela RN06, a plataforma não realiza vendas, então preço e disponibilidade ficam para uma versão futura. |
| Composição `Produtor` 1 → N `Produto` ("possui") | **Mantida** | `Produtor` contém a lista de `Produto`; no banco, `produto.produtor_id` com `ON DELETE CASCADE`. |
| Relação N:N produtor–feira (`produtor_feira`) | **Mantida** | Tabela de associação com chave composta, que impede participação duplicada (RN01, RN02). |

Os pilares exigidos (classe abstrata/interface, herança, polimorfismo com `@Override` e composição 1:N) continuam presentes; ver [Pilares de POO](#pilares-de-poo).

## Requisitos

| ID | Requisito | Descrição |
|----|-----------|-----------|
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

## Regras de negócio

| ID | Regra de negócio |
|----|------------------|
| RN01 | Um produtor pode participar de uma ou mais feiras. |
| RN02 | Uma feira pode possuir um ou mais produtores participantes. |
| RN03 | Um produto deve estar associado a um produtor cadastrado. |
| RN04 | Um produtor pode possuir diversos produtos. |
| RN05 | Uma feira deve possuir endereço e informações de funcionamento. |
| RN06 | A plataforma não realiza inicialmente vendas, pagamentos ou entregas. |
| RN07 | O contato com o produtor ocorrerá por meio das informações disponibilizadas por ele. |
| RN08 | O escopo inicial de utilização considera a Região Metropolitana de Maringá. |

## Cronograma de execução

### 1ª entrega (planejamento)

Igual ao documento entregue ([`docs/AEP_1a_entrega.pdf`](docs/AEP_1a_entrega.pdf), seção 3):

| Sprint / Data | Épico | User Story | Responsável |
|---|---|---|---|
| Sprint 1 — 15/08–22/08 | Descoberta e requisitos | **COMO UM** integrante da equipe **EU QUERO** validar o problema, os ODS e os requisitos **PARA QUE** o escopo seja claro. | Todos |
| Sprint 2 — 23/08–29/08 | Modelagem | **COMO UM** desenvolvedor **EU QUERO** definir as regras, entidades e relacionamentos **PARA QUE** o banco seja consistente. | Integrantes 1 e 2 |
| Sprint 3 — 30/08–05/09 | Arquitetura e POO | **COMO UM** desenvolvedor **EU QUERO** modelar classes com herança e polimorfismo **PARA QUE** a solução atenda às exigências de POO. | Integrante 3 |
| Sprint 4 — 06/09–10/09 | Documentação e GitHub | **COMO UMA** equipe **EU QUERO** finalizar diagramas, README e repositório **PARA QUE** a entrega seja reproduzível e avaliável. | Todos |

Integrante 1: Karoliny · Integrante 2: Junia · Integrante 3: Ezequiel.

### 2ª entrega (implementação)

| Data | Atividade | Responsável |
|------|-----------|-------------|
| 13/11/2026 | Produtores (RF01, RF02, RF10), login e infraestrutura | Ezequiel |
| 13/11/2026 | Produtos (RF03, RF04, RF08) e script do banco | Junia |
| 13/11/2026 | Feiras e participação (RF05, RF06, RF07, RF09) e menu principal | Karoliny |
| 13/11/2026 | README, documentação e diagramas | Karoliny |

## Licença

Ver [LICENSE](LICENSE).
