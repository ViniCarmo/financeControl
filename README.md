Readme · MD
Copiar

# Finance Control
 
API RESTful de controle financeiro pessoal desenvolvida com Java 21 e Spring Boot 3. Permite que usuários se cadastrem, autentiquem via JWT e gerenciem suas transações financeiras — registrando entradas e saídas — com geração de resumos mensais. Cada usuário acessa apenas os próprios dados.
 
---
 
## Tecnologias
 
**Backend**
 
- Java 21
- Spring Boot 3
- Spring Security + JWT (Auth0)
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway — migrations de banco de dados
- SpringDoc / Swagger — documentação interativa da API
- BCrypt — criptografia de senhas
- Lombok
- Maven
- Docker + Docker Compose
- JUnit 5 + Mockito — testes unitários e de integração
- H2 — banco em memória utilizado no perfil de testes
 
**Frontend**
 
O projeto conta com um dashboard web desenvolvido separadamente com React, TypeScript e Tailwind CSS, gerado com auxílio do [Cursor](https://www.cursor.com/). Mais detalhes em [`/frontend`](./frontend).
 
---
 
## Funcionalidades
 
- Cadastro e autenticação de usuários com JWT
- CRUD completo de transações financeiras (receitas e despesas)
- Geração de resumos mensais com totais de receita, despesa e saldo
- Paginação nos endpoints de listagem
- Isolamento de dados por usuário autenticado
- Documentação interativa via Swagger UI
 
---
 
## Como rodar com Docker
 
**Pré-requisitos:** Docker e Docker Compose instalados.
 
```bash
# Clone o repositório
git clone https://github.com/seu-usuario/finance-control.git
cd finance-control
 
# Suba a API e o banco de dados
docker-compose up --build
```
 
A API estará disponível em `http://localhost:8080`.
 
---
 
## Como rodar localmente
 
**Pré-requisitos:** Java 21, Maven e PostgreSQL instalados.
 
```bash
# Clone o repositório
git clone https://github.com/seu-usuario/finance-control.git
cd finance-control
```
 
Configure as variáveis no `application.yaml`:
 
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_control
    username: seu_usuario
    password: sua_senha
 
api:
  security:
    token:
      secret: ${JWT_SECRET_KEY:sua_chave_secreta}
```
 
```bash
# Compile e rode
./mvnw spring-boot:run
```
 
---
 
## Variáveis de ambiente
 
| Variável                     | Descrição                             | Padrão     |
|------------------------------|---------------------------------------|------------|
| `JWT_SECRET_KEY`             | Chave secreta para assinar tokens JWT | `12345678` |
| `SPRING_DATASOURCE_URL`      | URL de conexão com o banco            | —          |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco                      | —          |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco                        | —          |
 
---
 
## Documentação da API
 
Com a aplicação rodando, acesse:
 
```
http://localhost:8080/swagger-ui.html
```
 
Para testar endpoints protegidos no Swagger:
1. Faça login em `POST /auth` e copie o token retornado
2. Clique em **Authorize** e cole o token
 
---
 
## Endpoints principais
 
### Autenticação
 
| Método | Rota     | Descrição           | Auth |
|--------|----------|---------------------|------|
| `POST` | `/auth`  | Login — retorna JWT | ❌   |
| `POST` | `/users` | Cadastro de usuário | ❌   |
 
### Usuário
 
| Método   | Rota        | Descrição                  | Auth |
|----------|-------------|----------------------------|------|
| `GET`    | `/users/me` | Dados do usuário logado    | ✅   |
| `PUT`    | `/users/me` | Atualizar dados do usuário | ✅   |
| `DELETE` | `/users/me` | Deletar conta              | ✅   |
 
### Transações
 
| Método   | Rota                 | Descrição                   | Auth |
|----------|----------------------|-----------------------------|------|
| `GET`    | `/transactions`      | Listar transações paginadas | ✅   |
| `POST`   | `/transactions`      | Criar transação             | ✅   |
| `GET`    | `/transactions/{id}` | Buscar transação por ID     | ✅   |
| `PUT`    | `/transactions/{id}` | Atualizar transação         | ✅   |
| `DELETE` | `/transactions/{id}` | Deletar transação           | ✅   |
 
### Resumos
 
| Método | Rota                 | Descrição                 | Auth |
|--------|----------------------|---------------------------|------|
| `GET`  | `/summary`           | Listar resumos do usuário | ✅   |
| `POST` | `/summary/generate/` | Gerar resumo do mês atual | ✅   |
 
---
 
## Exemplo de uso
 
**Cadastro:**
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"username": "vinicius", "email": "vinicius@email.com", "password": "123456"}'
```
 
**Login:**
```bash
curl -X POST http://localhost:8080/auth \
  -H "Content-Type: application/json" \
  -d '{"email": "vinicius@email.com", "password": "123456"}'
```
 
**Criar transação (com token):**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"value": 1500.00, "type": "INCOME", "date": "2026-03-18", "description": "Salário"}'
```
 
---
 
## Testes
 
```bash
./mvnw test
```
 
O projeto possui testes unitários para os services e testes de integração para os controllers, utilizando H2 em memória no perfil de teste.
 
---
 
## Estrutura do projeto
 
```
src/
├── main/
│   ├── java/com/vinicius/finance_api/
│   │   ├── controller/       # Controllers REST
│   │   ├── dto/              # DTOs de request e response
│   │   ├── entity/           # Entidades JPA
│   │   ├── enums/            # Enumerações
│   │   ├── infra/
│   │   │   ├── exceptions/   # Exception handler global
│   │   │   ├── security/     # Spring Security, JWT, filtros
│   │   │   └── springdoc/    # Configuração do Swagger
│   │   ├── repositories/     # Interfaces JPA
│   │   └── service/          # Regras de negócio
│   └── resources/
│       ├── db/migration/     # Scripts Flyway
│       └── application.yaml
├── test/                     # Testes unitários e de integração
└── frontend/                 # Dashboard web (React + TypeScript)
```
## Deploy

| Serviço | URL |
|---------|-----|
| API (Render) | https://finance-api-drzx.onrender.com |
| Frontend (Vercel) | https://finance-control-theta-lemon.vercel.app |
| Swagger UI | https://finance-api-drzx.onrender.com/swagger-ui.html |

> O plano gratuito do Render hiberna após 15 minutos de inatividade. A primeira requisição pode demorar até 50 segundos.
 
---
 
## Autor
 
**Vinicius Carmo**  
[LinkedIn](https://www.linkedin.com/in/viniciuscarmoo/)
