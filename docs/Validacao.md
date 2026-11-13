# Validação - versão de console

Data: 13/11/2026. JDK Microsoft 21.0.12, Maven 3.9.11, Spring Boot 3.5.7 sem servidor web e MySQL real (imagem mysql:8.4), banco separado raizes_console, porta 3308.

## Testes automatizados

**16 testes: zero falhas, zero erros e zero ignorados.** CRUD de produtores, produtos e feiras; relacionamentos e cascatas; bloqueio de órfãos/duplicidades; validações; polimorfismo; encapsulamento; menus dos três módulos; entrada numérica incorreta; cancelamento de exclusão; login correto/incorreto; fim de entrada sem loop.

## JAR e terminal

Cinco processos Java separados executaram o cenário completo: cadastrar produtor; cadastrar produto e feira; associar e consultar participação; reiniciar e consultar os dados preservados; excluir os cadastros fictícios. O resultado está em validacao-terminal.json. A saída e a leitura do terminal usam UTF-8 para preservar acentos.

Os dados fictícios criados por esse cenário foram removidos ao final. O banco não contém cadastros reais pré-carregados. Os testes automatizados usam transação com rollback; sequências de ID podem avançar.

## Limites

O fluxo de terminal foi validado; F5 dentro de uma sessão aberta do VS Code ainda precisa ser conferido em cada computador. Não houve envio ao professor. O teste de acesso inicialmente retornou HTTP 403. Após o convite de colaborador, o acesso da conta Ezequielmarques261 foi confirmado em 13/11/2026. A branch do Ezequiel é parcial; os 16 testes foram executados na base local completa dos três módulos. Não foram criadas contribuições em nome de Junia ou Karoliny.

## Inicializadores Windows

Os dois comandos iniciar.ps1 e iniciar.ps1 -Pronto foram executados com êxito: o primeiro compilou e abriu o menu via Maven; o segundo abriu o JAR preparado. Ambos aceitaram entrada e encerraram normalmente.
