# 📦 Desafio Técnico – Sistema de Controle de Produção (Autoflex)

Este projeto foi desenvolvido como solução para um **desafio técnico**, com o objetivo de criar um sistema WEB para **controle de produtos, matérias-primas e sugestão de produção**, considerando o estoque disponível e priorizando produtos de maior valor.

O sistema foi construído seguindo boas práticas de arquitetura, separação de responsabilidades e com foco em legibilidade, organização e facilidade de manutenção.

---

## 🧠 Visão Geral da Solução

O sistema permite:

- Cadastro e manutenção de **Produtos**
- Cadastro e manutenção de **Matérias-primas**
- Associação entre **Produtos e Matérias-primas** (estrutura do produto)
- Cálculo automático da **Sugestão de Produção**, indicando:
  - Quais produtos podem ser produzidos
  - Quantas unidades de cada produto
  - Subtotal por produto
  - Valor total estimado da produção

A sugestão de produção **prioriza produtos de maior valor**, respeitando o consumo correto do estoque disponível.

---

## 🛠️ Tecnologias Utilizadas

### 🔙 Back-end
- Java 17  
- Spring Boot 4  
- Spring Data JPA  
- Hibernate  
- Flyway (controle de migrações do banco)  
- PostgreSQL  
- Swagger / OpenAPI (springdoc)  
- Lombok  
- Maven  
- JUnit 5 + Mockito (testes unitários)

### 🔜 Front-end
- Vue.js 3 (Composition API)  
- TypeScript  
- Vite  
- Axios  
- Tailwind CSS  
- Vue Router  

### 🐳 Infraestrutura
- Docker  
- Docker Compose  

---

## 🧱 Arquitetura do Back-end

O back-end foi estruturado seguindo uma arquitetura em camadas:

````bash
controllers → endpoints REST
services → regras de negócio
repositories → acesso a dados (JPA)
entities → entidades JPA
dtos → contratos de entrada e saída
mappers → conversão Entity ↔ DTO
````


### Destaques técnicos
- Uso de **DTOs explícitos** para requests e responses
- Validações com **Bean Validation**
- Tratamento global de exceções via `@ControllerAdvice`
- Controle de integridade relacional no banco
- Uso de `orphanRemoval` e controle explícito de persistência para evitar inconsistências
- Testes unitários cobrindo a lógica crítica da **Sugestão de Produção**

---

## 🧮 Regra de Negócio – Sugestão de Produção

A lógica de sugestão de produção segue os seguintes critérios:

1. Produtos são ordenados por **valor unitário (decrescente)**
2. Para cada produto:
   - Calcula-se o máximo de unidades possíveis com base no estoque atual
   - O estoque é consumido conforme as unidades sugeridas
3. Produtos sem estrutura ou sem estoque suficiente são ignorados
4. O resultado retorna:
   - Lista de produtos sugeridos
   - Quantidade produzível
   - Subtotal por produto
   - Valor total da produção

Essa lógica garante **priorização financeira** e uso consistente do estoque.

---

## 🖥️ Front-end

O front-end foi desenvolvido como uma **Single Page Application (SPA)**, consumindo a API REST.

### Funcionalidades implementadas
- CRUD de Produtos
- CRUD de Matérias-primas
- Gerenciamento da estrutura do produto
- Tela de Sugestão de Produção
- Feedback visual de loading, sucesso e erro
- Layout responsivo com Tailwind CSS

---

## ▶️ Como Executar o Projeto

### Pré-requisitos
- Docker
- Docker Compose
- Node.js (para executar o front-end)

---

### 🔹 Subindo o Back-end + Banco de Dados

Na pasta do back-end:

```bash
docker-compose up --build
```

### A API estará disponivel em:

````bash
http://localhost:8080
````

### Swagger (documentação da API):

````bash
http://localhost:8080/swagger-ui.html
````
- OBS: Recomendo a utilização do Swagger nos testes manuais, pois o configurei para facilitar o trabalho.

### 🔹 Executando o Front-end

- Na pasta do front-end:

````bash
npm install
npm run dev
````

### A aplicação estará disponível em:

````bash
http://localhost:5173
````

## 🧪 Testes

- Foram implementados testes unitários no back-end, com foco nas regras de negócio mais críticas, especialmente a Sugestão de Produção.
- Os testes validam cenários como:
  - Priorização por valor
  - Consumo correto do estoque
  - Casos sem estoque suficiente
  - Empate de valor entre produtos


### Sobre testes E2E (Cypress)

O Cypress não foi implementado neste projeto, pois no momento não é uma tecnologia que utilizo no meu dia a dia e não me senti confortável em aplicá-la sem garantir a qualidade esperada.

No entanto, tenho total interesse em aprender e aplicar Cypress, e encaro essa tecnologia como um próximo passo natural de evolução, especialmente em projetos front-end mais robustos.

## 📌 Considerações Finais

Este projeto foi desenvolvido com foco em:

- Clareza de código
- Organização
- Boas práticas
- Aderência total aos requisitos do desafio

Fico à disposição para explicar decisões técnicas, evoluir a solução ou implementar melhorias adicionais.
