Sistema de Votação em Pautas

- É possível fazer todas funções de um CRUD em uma pauta;
- Pode-se criar sessões de votação, é permitido somente 1 por pauta;
- Para votar em uma pauta, ela deve estar com a sessão aberta, as Opções de Votos são  somente YES/NO;
- É permitido somente um voto por CPF em cada pauta;
- Quando uma Pauta é deletar, a sessão da mesma também será deletada;
- Quando uma sessão é deletada, todos votos da mesma também serão deletados;
- O serviço faz integração com um outro serviço que verifica se o cpf está apto a votar;
- Foi configurado um swagger para melhor visualização da API;
- Foi utilizado Junit e Mockito para fazer testes unitários e de integração;
- Projeto parte de principio que terá um banco mongo conforme as configurações do application.yml;
- Para rodar, é necessário clonar e pode ser executado através de alguma IDE (IntelliJ, Eclipse);
- É possível rodar através do docker, acessando a pasta raiz via terminal e rodando o comando "docker-compose up --build";
