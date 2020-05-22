Sistema de Votação em Pautas

- É possível fazer todas funções de um CRUD em uma pauta;
- Pode-se criar sessões de votação, é permitido somente 1 por pauta;
- Para votar em uma pauta, ela deve estar com a sessão aberta, as Opções de Votos são  somente YES/NO;
- É permitido somente um voto por CPF em cada pauta;
- O serviço faz integração com um outro serviço que verifica se o cpf está apto a votar;

- Foi configurado um swagger para melhor visualização da API;
- Foi utilizado Junit e Mockito para fazer testes unitários e de integração;
- Projeto parte de principio que terá um banco mongo conforme as configurações do application.yml;
- Para rodar, é necessário clonar e rodar em alguma IDE (IntelliJ, Eclipse);