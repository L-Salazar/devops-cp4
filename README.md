## 👨‍💻 Autores

Leonardo Salazar - 557484

Alexsandro Macedo - 557068

# 🚀 Performance Kids - Docker Compose

Projeto da disciplina utilizando **Spring Boot + PostgreSQL** com **Docker Compose**.

## 📌 Pré-requisitos
- [Docker](https://docs.docker.com/get-docker/) instalado
- [Docker Compose](https://docs.docker.com/compose/install/) instalado
- (Opcional) [DBeaver](https://dbeaver.io/) ou PgAdmin para visualizar o banco


## Estrutura atual X estrutura futura

<img width="930" height="455" alt="image" src="https://github.com/user-attachments/assets/d4547b84-aa07-4ada-93ff-03569670d87f" />
<img width="1054" height="447" alt="image" src="https://github.com/user-attachments/assets/7810787f-612e-481c-960f-0fa511ee8aa2" />



---

## ▶️ Como rodar a aplicação

### 1. Subir os containers
Na pasta raiz do projeto (onde está o `docker-compose.yml`):

```bash
docker compose up -d --build
```

- `-d` roda em background
- `--build` garante que a imagem da aplicação será reconstruída

### 2. Verificar containers ativos
```bash
docker compose ps
```

Saída esperada:
```
NAME               SERVICE     STATUS         PORTS
postgres_local     database    Up            0.0.0.0:5432->5432/tcp
performance-kids   app         Up            0.0.0.0:8081->8081/tcp
```

### 3. Acessar a aplicação
- API: [http://localhost:8081](http://localhost:8081)  
- Healthcheck: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)

### 4. Acessar o banco
No DBeaver ou outro cliente PostgreSQL:
```
Host: localhost
Port: 5432
Database: performancekids
User: admin
Password: admin
```

---

## 🔑 Comandos essenciais do Docker Compose

- Subir containers em background:
  ```bash
  docker compose up -d
  ```

- Parar e remover containers (mantém o banco):
  ```bash
  docker compose down
  ```

- Parar e remover containers + volumes (zera o banco):
  ```bash
  docker compose down -v
  ```

- Ver logs da aplicação:
  ```bash
  docker compose logs -f app
  ```

- Entrar no container da aplicação:
  ```bash
  docker exec -it performance-kids sh
  ```

- Entrar no Postgres via psql:
  ```bash
  docker exec -it postgres_local psql -U admin -d performancekids
  ```

---

## ⚙️ Processo de Deploy passo a passo

1. Clone o repositório do projeto:
   ```bash
   git clone <url-do-repo>
   cd performancekids
   ```

2. Construa e suba os serviços:
   ```bash
   docker compose up -d --build
   ```

3. Confirme que estão ativos:
   ```bash
   docker compose ps
   ```

4. Acesse a API em `http://localhost:8081`  
   ou conecte no banco com `localhost:5432`.

---

## 🛠️ Troubleshooting básico

- **Porta 5432 já está em uso**
  - Outro Postgres está rodando.  
  - Solução: altere no `docker-compose.yml`:
    ```yaml
    ports:
      - "5433:5432"
    ```
    E acesse pelo `localhost:5433`.

- **Erro: UnsupportedClassVersionError**
  - Imagem de runtime com versão Java diferente da de build.  
  - Solução: mantenha JDK 21 no build **e** no runtime (`eclipse-temurin:21`).

- **Banco de dados apagado ao reiniciar**
  - Verifique se rodou `docker compose down -v` (isso apaga volumes).  
  - Para manter dados, use apenas:
    ```bash
    docker compose down
    ```

- **Não consigo digitar no console**
  - O comando `docker compose up` mostra **logs**, não é interativo.  
  - Para abrir terminal dentro do container, use:
    ```bash
    docker exec -it performance-kids sh
    ```

## Sobre o projeto

# Performance Kids 🎮⚽

## 📌 Descrição do Projeto
O **Performance Kids** é um sistema em **Spring Boot 3 (Java 21)** para gerenciamento de **brinquedos esportivos** voltados a crianças até 12 anos.  
A aplicação expõe uma API REST com **CRUD completo** de **Brinquedos**, **Categorias** e **Funcionários**, usando **DTOs**, **validações**, **HATEOAS** e documentação via **Swagger OpenAPI**.

- **IDE utilizada:** IntelliJ IDEA (pode ser executado também no Eclipse/NetBeans).
- **Porta padrão:** `8081`

---

## 🏗️ Tecnologias & Dependências
- Spring Boot 3 (Web, Data JPA, Validation, HATEOAS)
- Lombok
- Postgree
- Springdoc OpenAPI (Swagger UI)

---

## 📂 Estrutura de Pacotes
```
br.com.fiap.performancekids
 ┣ assembler        → ModelAssemblers (HATEOAS)
 ┣ controller       → REST Controllers (Brinquedo, Categoria, Funcionario)
 ┣ dto              → DTOs
 ┣ entity           → Entidades JPA
 ┣ repository       → Repositórios JPA
 ┣ service          → Regras de negócio
 ┗ PerformanceKidsApplication.java
```

---

## 🔑 Modelo de Dados (resumo)
### Brinquedo
- `id: Long`
- `nome: String`
- `categoria: Categoria (ManyToOne)`
- `classificacao: String` (`3-5`, `6-8`, `9-12`)
- `tamanho: String` (depende da categoria)
- `preco: BigDecimal`

### Categoria
- `id: Long`
- `nome: String` (`BOLA`, `TENIS`, `MEIA`, `ROUPA`, `RAQUETE`, `ACESSORIO`)
- `descricao: String` (opcional)

### Funcionario
- `id: Long`
- `nome: String`
- `email: String` (único)
- `senha: String`
- `cargo: String` (`ADMIN`, `OPERADOR`, ...)

---

## 🧠 Regras de Validação
- **Classificação** deve ser `3-5`, `6-8` ou `9-12`.
- **Categoria × Tamanho:**
  - `BOLA` → tamanho `3|4|5`
  - `ROUPA`/`MEIA` → `PP|P|M|G`
  - `TENIS` → `28..36`
- **Preço** `> 0`

---

## 🌐 Endpoints (CRUD) — com DTO & HATEOAS

### 1) Brinquedos
**POST `/brinquedos`** — cria
```json
{
  "nome": "Bola de Futebol Infantil",
  "categoriaId": 1,
  "classificacao": "6-8",
  "tamanho": "4",
  "preco": 79.90
}
```
**Resposta 201**
```json
{
  "id": 1,
  "nome": "Bola de Futebol Infantil",
  "categoriaId": 1,
  "categoriaNome": "BOLA",
  "classificacao": "6-8",
  "tamanho": "4",
  "preco": 79.90,
  "_links": {
    "self": { "href": "http://localhost:8081/brinquedos/1" },
    "lista": { "href": "http://localhost:8081/brinquedos" },
    "atualizar": { "href": "http://localhost:8081/brinquedos/1" },
    "deletar": { "href": "http://localhost:8081/brinquedos/1" }
  }
}
```

**GET `/brinquedos`** — lista (CollectionModel<EntityModel<DTO>>)

**GET `/brinquedos/{id}`** — busca por ID (EntityModel<DTO>)

**PUT `/brinquedos/{id}`** — atualiza
```json
{
  "nome": "Bola de Futebol Kids",
  "categoriaId": 1,
  "classificacao": "6-8",
  "tamanho": "4",
  "preco": 89.90
}
```

**DELETE `/brinquedos/{id}`** — remove (204 No Content)

---

### 2) Categorias
**POST `/categorias`**
```json
{
  "nome": "BOLA",
  "descricao": "Bolas esportivas para crianças"
}
```

**GET `/categorias`**, **GET `/categorias/{id}`**, **PUT `/categorias/{id}`**, **DELETE `/categorias/{id}`**

---

### 3) Funcionários
**POST `/funcionarios`**
```json
{
  "nome": "Ana Souza",
  "email": "ana@performancekids.com",
  "senha": "12345678",
  "cargo": "OPERADOR"
}
```

**GET `/funcionarios`**, **GET `/funcionarios/{id}`**, **PUT `/funcionarios/{id}`**, **DELETE `/funcionarios/{id}`**

---

## 🔗 HATEOAS — como usamos
- Controllers retornam **`EntityModel<DTO>`** (um recurso com `_links`)
- Listagens retornam **`CollectionModel<EntityModel<DTO>>`**
- Os links são montados por **Assemblers** (`BrinquedoModelAssembler`, etc.) com:
```java
linkTo(methodOn(BrinquedoController.class).buscarPorId(dto.getId())).withSelfRel();
```

---

## 🧪 Exemplos via cURL
**Criar Categoria**
```bash
curl -X POST http://localhost:8081/categorias \
 -H "Content-Type: application/json" \
 -d '{"nome":"BOLA","descricao":"Bolas esportivas"}'
```

**Criar Brinquedo**
```bash
curl -X POST http://localhost:8081/brinquedos \
 -H "Content-Type: application/json" \
 -d '{"nome":"Bola de Futebol Infantil","categoriaId":1,"classificacao":"6-8","tamanho":"4","preco":79.90}'
```

**Listar Brinquedos**
```bash
curl http://localhost:8081/brinquedos
```

**Atualizar Brinquedo**
```bash
curl -X PUT http://localhost:8081/brinquedos/1 \
 -H "Content-Type: application/json" \
 -d '{"id":1,"nome":"Bola Kids Pro","categoriaId":1,"classificacao":"6-8","tamanho":"4","preco":89.90}'
```

**Deletar Brinquedo**
```bash
curl -X DELETE http://localhost:8081/brinquedos/1
```

---

## 📖 Swagger (OpenAPI)
Acesse a documentação em:
```
http://localhost:8081/swagger-ui.html
```

---

## ▶️ Como executar
1. **Configurar** `application.properties` com seu Oracle.
2. **Rodar** pelo IntelliJ: `BrinquedosRevisaoApplication` (ou `PerformanceKidsApplication`), ou via Maven:
   ```bash
   mvn spring-boot:run
   ```
3. Testar no Postman/Insomnia ou pelo Swagger UI.

---
