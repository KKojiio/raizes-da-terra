# Junia - Produtos e esquema do banco

**Requisitos:** RF03, RF04 e RF08; RN03, RN04 e RN06.

## Seus arquivos

- `src/main/java/br/edu/raizes/domain/Produto.java`
- `src/main/java/br/edu/raizes/console/MenuProdutos.java`
- `src/main/java/br/edu/raizes/repository/ProdutoRepository.java`
- `database/schema.sql`

## O que você precisa revisar e testar

- [ ] Entender MenuProdutos → CatalogoService → ProdutoRepository → tabela produto.
- [ ] Testar cadastro/consulta/edição/exclusão; produto deve exigir produtor existente.
- [ ] Conferir FK, NOT NULL, ON DELETE CASCADE e chave composta produtor_feira no schema.sql.
- [ ] Manter pagamentos, vendas e entregas fora do escopo. Ao editar produto, preservar o proprietário.

## Como abrir e executar

1. Extraia o ZIP inteiro. No VS Code, use Arquivo > Abrir Pasta e escolha a pasta que contém pom.xml. Não abra somente Minha parte nem arquivos .java avulsos.
2. Tenha JDK 21, Docker Desktop e Extension Pack for Java. Aceite a recomendação da pasta .vscode.
3. No terminal integrado, execute `powershell -ExecutionPolicy Bypass -File .\iniciar.ps1`. Isso compila e executa o código-fonte; use esta opção ao alterar código.
4. Para apenas demonstrar o JAR pronto, execute `powershell -ExecutionPolicy Bypass -File .\iniciar.ps1 -Pronto`. Essa opção não recompila suas alterações.
5. O catálogo abre no próprio terminal. Administração: opção 4, usuário admin. A senha é ADMIN_PASSWORD no arquivo .env gerado localmente. Não envie esse arquivo.
6. Cadastre primeiro o produtor, depois seu produto. Cadastre a feira e associe o produtor. Os IDs aparecem ao cadastrar/listar.
7. Para testes, execute `powershell -ExecutionPolicy Bypass -File .\testar.ps1`. São usados MySQL real e transações revertidas ao final dos testes.

## Sobre seu commit

Este pacote foi preparado com assistência e contém a base completa para todos conseguirem executar. A indicação de responsabilidade não comprova que você já fez ou compreendeu o trabalho. Execute, revise, faça melhorias/correções reais e descreva seu trabalho no commit. Não crie commits vazios ou em nome de outra pessoa.

O acesso da conta Ezequielmarques261 já foi confirmado. Sua parte usa a branch ezequiel-console. Junia e Karoliny também precisam de acesso para enviar seus próprios arquivos. A main só estará completa depois da integração dos três conjuntos.

No GitHub, é necessário coordenar a primeira integração dos três conjuntos de arquivos. Todos os ZIPs têm a mesma base: não subir três projetos duplicados nem substituir alterações recentes dos colegas. Crie uma branch própria a partir da versão atual do repositório, copie/revise somente sua parte e faça o commit dela. Até os três módulos estarem integrados, um conjunto isolado pode não compilar; teste a base completa fornecida.

## Lista de arquivos para revisar antes do git add

Confira git status e git diff. Adicione apenas os caminhos da lista “Seus arquivos” e o seu registro de revisão em docs/revisoes/. Use git diff --cached para conferir antes de criar o commit. O guia geral de GitHub está em Entregas/Guia-GitHub-Equipe-Raizes-da-Terra.pdf no cofre.

## Registro de revisão real

Crie `docs/revisoes/Junia.md` quando executar. Informe data, o que testou, resultado e dúvidas. Esse arquivo não foi preenchido por você ainda e não deve conter resultados inventados.

## Base publicada para continuar

Parte de Ezequiel: commit adde501, branch ezequiel-console, confirmada no remoto em 13/11/2026. A main não foi alterada.

Clone a branch ezequiel-console em uma pasta separada, copie a base completa do seu ZIP preservando .git e crie sua branch de trabalho (Junia: junia/produtos-banco; Karoliny: karoliny/feiras-participacao). Compare atualizações remotas antes de substituir arquivos. Adicione somente seus arquivos. O PDF incluído no pacote explica os comandos.
