# Projeto Escalante

## Visão Geral

O Projeto Escalante tem o objetivo principal de automatizar a criação de escalas de serviço operacional dos militares de um quartel do CBMCE. O sistema visa otimizar o processo de elaboração das escalas, reduzindo o tempo gasto pelo escalante e garantindo o cumprimento das regras de negócio específicas do [domínio](docs/DominioProjeto.md).

A versão atual do projeto trata-se de um MVP focado no Contexto Escalante e cobre as seguintes funcionalidades:

- Importação dos dados de militares a partir de uma planilha no formato .xlsx;
- Exibição e manipulação dos dados de militares;
- Importação da escala do mês anterior a partir de uma planilha no formato .xlsx;
- Geração automática de escalas, considerando as datas de início e fim, a quantidade de dias de serviço por equipe, a escala anterior importada e a distribuição dos militares disponíveis conforme as [regras de negócio](docs/regrasNegocio/ContextoEscalante.md);
- Exibição e manipulação das escalas geradas;
- Exportação da escala gerada em formato .xlsx.

## Estrutura do Repositório

Raiz do projeto (principais pastas):

- `apis/` - Backend Java (Spring Boot)
  - `src/main/java/.../controllers/` - Controllers REST (endpoints)
  - `src/main/java/.../dtos/` - DTOs usados nas APIs
  - `src/main/java/.../usecases/`, `domain/`, `services/`, `utils/` - lógica de aplicação e domínio
- `front/` - Frontend (Vite + React)
  - `src/` - código fonte React (components, clients, routes, context)
  - `package.json`, scripts de desenvolvimento e build
- `docs/` - Documentação de domínio, regras de negócio e diagramas
- `regrasNegocio/` - arquivos de regras específicas (ContextoEscalante, ContextoAjudancia)

## Features Implementadas

1. Obtenção de planilha modelo para importação de militares (backend)
  - Saída: um template XLSX (`modelo_importacao_militares.xlsx`) com colunas esperadas para importação.

2. Importação de militares via planilha XLSX (backend)
  - Entrada: arquivo XLSX com lista de militares (colunas: matrícula, nome, patente, antiguidade, nascimento, folga especial, C.O.V.).
  - Processamento: validação dos dados e extração para lista de militares escaláveis.
  - Saída: lista de militares escaláveis.

3. Importação da escala do mês anterior via planilha XLSX (backend)
  - Entrada: arquivo XLSX com a escala do mês anterior (colunas: data do serviço, matrícula, militar escalado (nome de paz), patente, antiguidade, função, folga).
  - Processamento: validação dos dados e extração para lista de serviços operacionais anteriores.
  - Saída: lista de serviços operacionais anteriores.

4. Geração automática de escala (backend)
  - Entrada: período (data de início/fim), número de dias de serviço por equipe, lista de militares e lista de serviços operacionais anteriores.
  - Processamento: aplicação das regras de negócio para distribuir militares nas funções diárias.
  - Saída: escala de serviços operacionais por dia com atribuição de um militar para cada função.

5. Exportação de escala para XLSX (backend)
  - Entrada: escala gerada (lista de serviços operacionais).
  - Processamento: formatação dos dados em planilha no formato XLSX.
  - Saída: um arquivo no formato XLSX contendo a escala pronta para download.

6. Frontend (SPA) — interface de interação com o usuário
  - Componente para download do modelo, upload e processamento da planilha de militares e escala anterior.
  - Formulário para criação de escalas (datas, dias de serviço, lista de militares e lista de serviços operacionais anteriores).
  - Tabelas dos militares e escalas geradas, contendo opções para exclusão e alteração de itens.
  - Componentes de feedback (toasts), modais de confirmação e barra de navegação lateral.
  - Botões de ação para adição de serviços e exportação da escala.
  - Clientes HTTP baseados em fetch para comunicação com o backend.

## Endpoints API

Base URL (padrão em execução local): `http://localhost:8080`

1) `GET /api/militar/modelo/xlsx`
  - Descrição: Retorna o template XLSX para importação de militares (`modelo_importacao_militares.xlsx`).
  - Response: `200 OK` com arquivo XLSX e cabeçalho `Content-Disposition: attachment; filename=modelo_importacao_militares.xlsx` ou `503` em caso de erro.

2) `POST /api/militar/importar/xlsx`
  - Descrição: Recebe um arquivo Multipart/form-data (campo `militares`) contendo a planilha de militares, valida e retorna a lista de militares escaláveis extraídos.
  - Form-data key: `militares` (tipo: file)
  - Response: `200 OK` com a lista de militares processados ou `503` em caso de erro.
  - Exemplo de response body:
```json
[
 {  "antiguidade": 10,
  "matricula": "12345",
  "patente": "CB",
  "nomePaz": "FULANO",
  "nascimento": "1990-01-01",
  "folgaEspecial": 0,
  "cov": false
 }
]
```

3) `GET /api/escala/modelo/xlsx`
  - Descrição: Retorna o template XLSX para importação da escala do mês anterior (`modelo_importacao_escala.xlsx`).
  - Response: `200 OK` com arquivo XLSX e cabeçalho `Content-Disposition: attachment; filename=modelo_importacao_escala.xlsx` ou `503` em caso de erro.

4) `POST /api/escala/importar/xlsx`
  - Descrição: Recebe um arquivo Multipart/form-data (campo `escala`) contendo a planilha da escala do mês anterior, valida e retorna a lista de serviços operacionais extraídos.
  - Form-data key: `escala` (tipo: file)
  - Response: `200 OK` com a lista de serviços operacionais processados ou `503` em caso de erro.
  - Exemplo de response body:
```json
[
 { 
  "dataServico": "2024-08-31",
  "matricula": "12345",
  "nomePaz": "FULANO",
  "patente": "CB",
  "antiguidade": 10,
  "funcao": "Operador de Linha",
  "folga": false
 }
]
```

5) `POST /api/escala`
  - Descrição: Gera a escala automática com base nos parâmetros e lista de militares.
  - Content-Type: `application/json`
  - Exemplo de request body:

```json
{
  "dataInicio": "2024-09-01",
  "dataFim": "2024-09-30",
  "diasServico": 2,
  "militares": [
   {
    "antiguidade": 10,
    "matricula": "12345",
    "patente": "CB",
    "nomePaz": "FULANO",
    "nascimento": "1990-01-01",
    "folgaEspecial": 0,
    "cov": false
   }
  ],
  "servicosAnteriores": [
   {
    "dataServico": "2024-08-31",
    "matricula": "12345",
    "nomePaz": "FULANO",
    "patente": "CB",
    "antiguidade": 10,
    "funcao": "Operador de Linha",
    "folga": false
   }
  ]
}
```
  - Response: `200 OK` com representação da escala ou `503` em erro.
  - Exemplo de response body:
```json
[
  {
    "dataServico": "2024-09-01",
    "matricula": "12345",
    "nomePaz": "FULANO",
    "patente": "CB",
    "antiguidade": 10,
    "funcao": "Operador de Linha",
    "folga": false
  }
]
```

6) `POST /api/escala/exportar/xlsx`
  - Descrição: Recebe a escala (lista de `ServicoOperacionalDto`) e retorna um arquivo .xlsx (`escala.xlsx`) pronto para download.
  - Content-Type: `application/json`
  - Exemplo de request body:
```json
[
  { 
    "dataServico": "2024-09-01",
    "matricula": "12345",
    "nomePaz": "FULANO",
    "patente": "CB",
    "antiguidade": 10,
    "funcao": "Operador de Linha",
    "folga": false
  }
]
```
  - Response: `200 OK` com `Content-Disposition: attachment; filename=escala.xlsx` e MIME `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` ou `503` em caso de erro.

## Como Executar Localmente

Pré-requisitos gerais:

- Java 21+
- Maven 3.9+
- Node.js 18+ e NPM 10+

1) Backend (API)

No terminal, a partir da raiz do repositório:

```bash
cd apis
# instalar dependências do Maven e realizar build (opcional)
mvn clean package -DskipTests
# executar a aplicação
mvn spring-boot:run
# ou, após gerar o jar (opcional):
java -jar target/escalante-0.0.1-SNAPSHOT.jar
```

A API estará disponível em `http://localhost:8080` (por padrão).

2) Frontend (UI)

No terminal, a partir da raiz do repositório:

```bash
cd front
# instalar dependências
npm install
# inicia Vite em modo dev (localhost:5173 por padrão)
npm run dev
# para realizar o build (opcional)
npm run build
```

O frontend estará disponível em `http://localhost:5173` (por padrão).

O frontend consome os endpoints do backend em `http://localhost:8080`. Caso o backend rode em outra porta/host, crie um arquivo `front/.env` com a variável `VITE_API_URL` apontando para a URL correta.

## Fluxo de Utilização Recomendado

1. Acesse a aplicação frontend no navegador.
2. Na aba Militares: Baixe o modelo, preencha com seu efetivo e faça a importação.
3. Na aba Escala:
  - (Opcional) Baixe o modelo de "Escala Anterior", preencha com os serviços do mês anterior e faça a importação.
  - Preencha as datas da nova escala, a quantidade de dias de serviço e clique em Gerar Escala.
4. Revise a escala gerada, faça ajustes manuais se necessário.
5. Exporte a escala finalizada em formato XLSX para distribuição.

## Tecnologias Utilizadas

- Backend
  - Java 21
  - Spring Boot 3.5.x
  - Maven 3.9.x
  - Apache POI
  - JUnit

- Frontend
  - Node.js 22.2.0
  - NPM 10.9.x
  - Vite 7.2.2
  - React 19
  - React Router DOM