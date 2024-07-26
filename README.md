# PManager - Sistema de Gerenciamento de Pessoas

## Pré-requisitos 
- Docker (Obrigatório)
- Java 21 (Caso queira rodar separadamente)
- Node 16+ (Caso queira rodar separadamente)

## Detalhamento

### ☕ Backend

#### Tecnologias
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Data Redis
- Spring Security
- Spring Boot Test (JUnit)
- Lombok
- Swagger
- MySQL 
- H2 (banco de testes)

#### Resumo
- A API foi desenvolvida utilizando Spring Boot e Java 21, implementando um CRUD completo para gerenciamento de **Pessoas** e registro e autenticação de **Usuários**. Além disso, foram incluídos testes unitários, testes de integração, logs, cache e validação de CPF.

### 📱 Frontend

#### Tecnologias
- Javascript
- Vue
- Vuetify
- Pinia

#### Resumo
- O Frontend consome todos os endpoints da API, incluindo validações, máscaras e uma interface amigável, permitindo que o usuário se registre, faça login e execute todas as operações relacionadas a pessoas.

### 🪖 Infra e Ferramentas
- Docker
- Postman

## Passos para Rodar o Projeto

### Clonar o Repositório

```sh
git clone https://github.com/marquesdavi/people-manager.git
cd people-manager
```

### Construir e Iniciar os Contêineres

Execute o seguinte comando na raiz do projeto, onde o arquivo `docker-compose.yaml` está localizado:

```sh
docker-compose up --build
```

Este comando irá:
- Construir as imagens Docker para o backend e frontend.
- Iniciar o contêiner MySQL.
- Esperar até que o MySQL esteja pronto.
- Iniciar o contêiner backend, que se conectará ao MySQL.
- Iniciar o contêiner frontend, que dependerá do backend.

### Acessar o Frontend

Abra um navegador e navegue até `http://localhost:5173/`.

### Acessar o Backend

O backend estará disponível em `http://localhost:8080/`.

### Parar os Contêineres

Para parar os contêineres, pressione `Ctrl+C` no terminal onde os contêineres estão sendo executados ou execute:

```sh
docker-compose down
```

## Endpoints Backend

### Autenticação
- **POST - /validate-token**
  - Valida um token JWT
- **POST - /login**
  - Retorna um token JWT

### Usuário
- **GET - /user/**
  - Obtém todos os usuários (Exclusivo para ADMIN)
- **POST - /user/**
  - Cria um novo usuário

### Pessoa
- **GET - /person/**
  - Obtém todas as pessoas com paginação
- **POST - /person/**
  - Cria uma nova pessoa
- **GET - /person/{id}**
  - Obtém uma pessoa por ID
- **DELETE - /person/{id}**
  - Exclui uma pessoa
- **PATCH - /person/{id}**
  - Atualiza uma pessoa existente

### Rota do Swagger
- **GET - /swagger-ui/index.html**
  - Interface de usuário do Swagger para visualização e teste dos endpoints

## Rotas Frontend

- **/**
    - Tela principal com todas as operações de CRUD
- **/login**
    - Tela para autenticação no sistema
- **/signup**
    - Tela para se registrar no sistema

## Possíveis dúvidas
- **Caso haja algum problema de inicialização:**
  - Verifique se as portas 8080, 5173 e 3307 não estão sendo utilizadas por algum outro serviço.
  - Caso alguma delas esteja, desligue os serviços que estão ocupando as portas.
  - Tente iniciar o projeto novamente.

- **Onde posso conseguir um CPF válido para testar?**
  - [4Devs - Gerador de CPF](https://www.4devs.com.br/gerador_de_cpf)