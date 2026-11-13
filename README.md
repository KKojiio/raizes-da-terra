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

Os dados ficam salvos no volume do Docker entre execuções. Para pausar o banco: `docker compose stop`.

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

São 16 testes de integração contra o **MySQL real** (sem banco em memória); cada teste roda numa transação desfeita ao final.

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

**Arquitetura em camadas:** `Menu* (controle) → CatalogoService (regras) → CatalogoRepository (interface) → ProdutorRepository / ProdutoRepository / FeiraRepository → MySQL`. Diagramas em [`docs/diagramas`](docs/diagramas).

## Pilares de POO

| Pilar | Onde |
|---|---|
| Classe abstrata | `ItemCatalogo` (método abstrato `getResumo()`) |
| Interface | `CatalogoRepository`, implementada por `JdbcCatalogoRepository` |
| Herança | `Produtor` e `Feira` estendem `ItemCatalogo` |
| Polimorfismo (`@Override`) | `Produtor` e `Feira` sobrescrevem `getResumo()`; o catálogo percorre uma `List<ItemCatalogo>` e chama `getResumo()` em cada item |
| Composição 1:N | Um `Produtor` possui uma lista de `Produto`; no banco, `produto.produtor_id` com `ON DELETE CASCADE` |
| Encapsulamento | Atributos `private final`, lista de produtos imutável |

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

| Data | Atividade | Responsável |
|------|-----------|-------------|
| 15/08/2026 | Levantamento e validação do problema | Todos |
| 18/08/2026 | Definição dos ODS e proposta de valor | Todos |
| 22/08/2026 | Definição e revisão dos requisitos | Todos |
| 25/08/2026 | Definição das regras de negócio | Karoliny |
| 29/08/2026 | Modelagem das entidades e DER | Junia |
| 03/09/2026 | Diagrama de classes | Ezequiel |
| 04/09/2026 | Definição da arquitetura e tecnologias | Todos |
| 04/09/2026 | Criação e organização do GitHub | Karoliny |
| 08/09/2026 | Revisão de consistência entre requisitos e diagramas | Todos |
| 10/09/2026 | Formatação e revisão final da 1ª entrega | Todos |

### 2ª entrega (implementação)

| Data | Atividade | Responsável |
|------|-----------|-------------|
| 13/11/2026 | Produtores (RF01, RF02, RF10), login e infraestrutura | Ezequiel |
| 13/11/2026 | Produtos (RF03, RF04, RF08) e script do banco | Junia |
| 13/11/2026 | Feiras e participação (RF05, RF06, RF07, RF09) e menu principal | Karoliny |
| 13/11/2026 | README, documentação e diagramas | Karoliny |

## Licença

Ver [LICENSE](LICENSE).
