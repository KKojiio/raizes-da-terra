# Entenda o console em um cadastro

1. Main inicia o Spring, que conecta ao MySQL e monta as dependências.
2. MenuPrincipal permite consultas e pede login antes da administração.
3. MenuProdutores lê o nome, descrição, telefone e localização usando Entrada.
4. CatalogoService verifica campos e inicia a transação. ID zero cria; outro ID edita um existente.
5. JdbcCatalogoRepository direciona a chamada ao ProdutorRepository.
6. O repositório usa INSERT/UPDATE com parâmetros; o texto digitado não vira parte do comando SQL.
7. O MySQL grava; o menu mostra confirmação. Ao consultar novamente, é feito SELECT real.

## Antes de programar

Crie uma feira e um produtor fictícios. Anote os IDs. Crie um produto ligado ao produtor e associe o produtor à feira. Consulte como consumidor, edite, saia do Java e abra novamente para conferir a persistência. Finalmente teste exclusões e confirme o que permanece.

## O que cada conceito significa aqui

Uma classe reúne dados e comportamentos. Produtor e Feira são tipos diferentes de ItemCatalogo: isso é herança. getResumo é implementado de formas diferentes com @Override, e o menu usa os dois como ItemCatalogo: isso é polimorfismo. O produtor controla o ciclo de vida dos seus produtos: composição. A tabela de participação guarda pares de IDs porque as duas entidades têm relação muitos para muitos.

## Erros de entrada

Entrada.numero repete a pergunta quando recebe letras. Entrada.tentar mostra mensagens de validação/banco sem expor SQL ou senha. Fim de entrada encerra sem loop infinito. Excluir exige confirmação. Três erros de login retornam ao catálogo; ninguém acessa os menus administrativos sem autenticar.

## Escopo do login

É uma conta administrativa definida por variável de ambiente para um programa local. Não é um serviço público de autenticação e não mantém cadastro de senhas no banco. Cadastro de usuários não foi exigido nos RF originais.

## Estudo de 15 minutos

Leia apenas o caminho de um cadastro, encontre os quatro métodos salvarProdutor, execute com dados fictícios e anote uma dúvida. Depois estude produtos e feiras. Não tente decorar o projeto inteiro de uma vez.
