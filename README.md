# Projeto Escalante
### **AINDA ESTA EM CONSTRUÇÃO!**

## Sobre o Projeto

Este é um projeto tem o intuito de criar um sistema web para automatizar a tarefa de criação de escalas mensais de trabalho para militares de um quartel do CBMCE.

## Pré-requisitos

* Ter o JDK 21 instalado na máquina.
* Acesso ao repositório do GitHub para baixar o projeto.
* VS Code, Eclipse ou STS instalados na máquina.
* No caso do VS Code ou Eclipse, será necessário instalar as extensões do Spring Boot, o `Spring Boot Extension Pack` no caso do VS Code e o `Spring Tools 4` no caso do Eclipse.
* Ter o Postman (ou similar) instalado na máquina para testar as funcionalidades da aplicação.

## Passo a Passo de Instalação e Execução

### Baixando o Projeto

1. Acesse este repositório no GitHub.
1. Clique em "Code" e selecione "Download ZIP".
1. Descompacte o arquivo baixado em uma pasta da sua escolha.

### Executando a Aplicação

1. Abra a pasta `api` dentro da raiz do projeto no seu editor de código.
1. No VS Code, na barra lateral, clique no icone do `Spring Boot Dashboard`, abrirá uma tela lateral com as configurações da aplicação.
1. Haverá um menu Apps, nele aparecerá o nome da aplicação `escalante`. Clique no ícone `run` ao lado  do nome da aplicação.
1. No Eclipse e STS, na barra superior, clique no icone do Spring Boot Dashboard, abrirá uma tela inferior com as configurações da aplicação.
1. Clique no nome da aplicação `escalante` e clique no ícone `run`, na barra imediatamente acima do nome da aplicação.

### Observações

* O projeto utiliza a dependência H2 para banco de dados em memória.
* A aplicação é configurada para rodar na porta 8081, verifique os endpoints que estão disponíveis no `Spring Boot Dashboard`.
* É recomendável criar um arquivo `.gitignore` para ignorar os arquivos gerados pelo Maven e IDEs.
* Informações sobre as regras de negócio do projeto podem ser encontradas na pasta `docs`.

## Dependências e Tecnologias Utilizadas

* Spring Boot 3.4.1
* Java 21
* Maven
* H2 (banco de dados em memória)
* JPA
* Lombok