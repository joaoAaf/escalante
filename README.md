# Projeto Escalante

## Visão Geral

O **Projeto Escalante** tem o objetivo principal de automatizar a criação de escalas de serviço operacional dos militares de um quartel do CBMCE (Corpo de Bombeiros Militar do Ceará). O sistema visa otimizar o processo de elaboração das escalas, reduzindo o tempo gasto pelo escalante e garantindo o cumprimento das regras de negócio complexas do domínio, como antiguidade, folgas e restrições de função.

O sistema evoluiu de um MVP focado apenas na geração de escalas para uma aplicação completa com **controle de acesso e segurança**.

### Tecnologias Utilizadas

* **Backend:** Java 21, Spring Boot 3.5+, Maven, Apache POI (manipulação de xlsx), Spring Security (JWT).
* **Cache:** Redis 8.4 (para gerenciamento de Blacklist de tokens).
* **Frontend:** React 19, Vite 7 e Node.js 22+.
* **Infraestrutura:** Docker e Docker Compose.

### Principais Desafios

* Implementação de regras de negócio complexas para alocação justa de militares.
* Manipulação e validação de arquivos binários (.xlsx) para importação e exportação de dados.
* Gerenciamento de estado de autenticação e segurança utilizando tokens JWT com invalidação via Redis (Blacklist).

## Estrutura do Repositório

A estrutura do projeto está organizada da seguinte forma:

* `apis/` - Backend Java (Spring Boot)
* `src/main/java/.../escalante/` - Núcleo da lógica de escalas e militares (Controllers, Models, Services).
* `src/main/java/.../seguranca/` - Módulo de autenticação, JWT e controle de usuários.
* `src/main/java/.../shared/` - Configurações globais e tratamento de exceções.

* `front/` - Frontend (Vite + React)
* `src/routes/` - Páginas da aplicação (Login, Escala, Militares, Usuários).
* `src/components/` - Componentes reutilizáveis.
* `src/clients/` - Camada de integração com a API.

* `docs/` - Documentação de domínio, diagramas (Draw.io), coleção do Postman e regras de negócio.
* `docker-compose.yml` - Orquestração dos containers (API, Frontend e Redis).

## Features Implementadas

O sistema cobre as seguintes funcionalidades, divididas por contexto:

**1. Contexto de Segurança**

* **Autenticação JWT:** Login seguro com geração de tokens de acesso.
* **Logout Seguro:** Invalidação de tokens utilizando Redis (Blacklist).
* **Gestão de Usuários:** Cadastro de novos usuários, alteração de senha, listagem e remoção.
* **Controle de Perfis:** Atribuição de perfis de acesso (ex: ADMIN, ESCALANTE).

**2. Contexto Escalante (Core)**

* **Importação de Militares:** Leitura de planilhas .xlsx para cadastro em massa do efetivo.
* **Importação de Escala Anterior:** Leitura da escala do mês passado para cálculo de folgas e restrições.
* **Geração Automática de Escala:** Algoritmo que distribui os militares disponíveis nas funções diárias respeitando regras de antiguidade, folgas e restrições de cada função.
* **Exportação:** Geração de arquivo .xlsx com a escala finalizada pronta para distribuição.
* **CRUD de Militares:** Implementação de banco de dados para persistência dos militares, permitindo operações de criação, leitura, atualização e exclusão.

## Endpoints API

Abaixo estão os principais endpoints da API. Com exceção do login e alteração de senha, todos os endpoints requerem autenticação via token JWT no cabeçalho `Authorization: Bearer seu_token_aqui`.

### Segurança e Usuários

* **POST** `/api/login` - Autentica o usuário.

  Utiliza de autenticação básica (Basic Auth) no cabeçalho:
  ```http
  Authorization: Basic base64(username:password)
  ```
  Retorna o token JWT:
  ```json
  {
      "bearerToken": "seu_token_jwt_aqui"
  }
  ```

* **POST** `/api/logout` - Realiza logout (invalida o token).

* **POST** `/api/usuarios` - Cadastra um novo usuário.
  
  Recebe o username e os perfis do usuário:
  ```json
  {
      "username": "usuario@teste.com",
      "perfis": ["escalante"]
  }
  ```
  Retorna o usuário criado e a senha temporária:
  ```json
  {
      "username": "usuario@teste.com",
      "perfis": ["escalante"],
      "senhaGerada": "senha1234"
  }
  ```
  Obs: A senha temporária, não gera token de autenticação, apenas permite que o usuário altere a senha.
  
* **PATCH** `/api/usuarios/password?novo=Senha123` - Atualiza a senha do usuário. Utiliza autenticação básica (Basic Auth) e a nova senha é passada por parâmetro.

### Militares

* **GET** `/api/militar/modelo/xlsx` - Baixa o modelo de importação.
* **POST** `/api/militar/importar/xlsx` - Importa a planilha de militares via upload.
* **GET** `/api/militar` - Lista todos os militares cadastrados.
* **POST** `/api/militar` - Cadastra lista de militares (JSON).
  
  Recebe uma lista de militares e não retorna nada:
  ```json
  [
    {
      "antiguidade": 10,
      "matricula": "REG00006",
      "patente": "CB",
      "nomePaz": "RAFAEL",
      "nascimento": "1990-06-06",
      "folgaEspecial": 0,
      "cov": false
    }
  ]
  ```
* **PUT** `/api/militar` - Atualiza os dados de um militar. Segue o mesmo payload do cadastro, mas recebendo apenas um militar.
* **DELETE** `/api/militar/{matricula}` - Remove o militar pela matrícula.

### Escalas

* **POST** `/api/escala/importar/xlsx` - Importa a planilha da escala do mês anterior.
* **POST** `/api/escala` - Gera a escala automática.
  
  Recebe os dados para geração da escala:
  ```json
  {
      "dataInicio": "2026-02-02",
      "dataFim": "2026-02-28",
      "diasServico": 2,
      "militares": [ ... ],
      "servicosAnteriores": [ ... ]
  }
  ```
  Retorna a escala gerada:
  ```json
  [
    {
        "dataServico": "2026-02-02",
        "matricula": "REG00003",
        "nomePaz": "JOÃO",
        "patente": "Sargento",
        "antiguidade": 7,
        "funcao": "Fiscal de Dia",
        "folga": 3
    },
    { ... }
  ]

* **POST** `/api/escala/exportar/xlsx` - Exporta a escala gerada para o formeto XLSX.

*Para ver todos os payloads detalhados, consulte o arquivo `docs/escalante.postman_collection.json`.*

## Como Executar Localmente

Você precisará configurar as variáveis de ambiente. Renomeie o arquivo `.env.example` na raiz para `.env` e edite-o para seu ambiente (para Docker) ou configure as variáveis no seu sistema operacional.

**Variáveis Necessárias:**

```properties
TZ=meu_fuso_horario
SERVER_NAME=server_name_nginx
API_URL=url_api_backend
PROFILE_ACTIVE=profile_ativo_spring
FRONT_URL=url_frontend
JWT_SECRET=sua_chave_secreta_32_bytes_no_minimo
JWT_EXPIRATION=tempo_de_expiracao_jwt_em_ms
USUARIO_INICIAL_USERNAME=email_do_usuario_inicial
REDIS_PASSWORD=sua_senha_redis
REDIS_HOST=host_redis
```

### Opção 1: Utilizando Docker (Recomendado)

Esta opção sobe toda a stack da aplicação automaticamente.

1. Certifique-se de ter o **Docker** e **Docker Compose** instalados.
2. Na raiz do projeto, execute:
```bash
docker-compose up -d
```

3. Acesse a aplicação:
* Frontend: `http://localhost:8000`

Obs: Caso queira acessar diretamente a API você deve adicionar o parâmetro `ports: - "8080:8080"` no serviço `api` do `docker-compose.yml`.

### Opção 2: Execução Manual (Sem Docker)

**Pré-requisitos:** Java 21, Maven, Node.js 18+ e uma instância do **Redis** rodando localmente.

#### 1. Redis

Inicie um servidor Redis localmente na porta 6379 com a senha configurada nas variáveis de ambiente.

#### 2. Backend (API)

1. Navegue até a pasta `apis`.
2. Configure as variáveis de ambiente no seu terminal.
3. Execute a aplicação:
```bash
mvn spring-boot:run
```
4. A API estará disponível em `http://localhost:8080`.

#### 3. Frontend

1. Navegue até a pasta `front`.
2. Instale as dependências:
```bash
npm install
```

3. Execute em modo de desenvolvimento:
```bash
npm run dev
```

4. O frontend estará disponível em `http://localhost:8000`.

## Implantação

A aplicação está atualmente implantada e acessível publicamente para testes e uso em produção.

* **URL de Acesso:** [https://escalante-infnet.appsemaperreio.com.br](https://escalante-infnet.appsemaperreio.com.br)

## Testes

O projeto conta com testes automatizados (unitários e de integração) no backend para garantir a integridade das regras de negócio e da segurança.

Para executar os testes do backend:

```bash
cd apis
mvn test
```

Os testes abrangem:

* Testes unitarios referentes a importação de planilhas XLSX (`ImportadorXLSXTest`).
* Testes unitários referentes a geração token JWT (`JwtServiceTest`).
* Testes de integração dos endpoints referentes à militar (`MilitarControllerIntegrationTest`).
* Testes de integração dos endpoints referentes à usuarios (`UsuarioControllerIntegrationTest`).
* Testes de integração do endpoint referente ao login (`LoginControllerIntegrationTest`).