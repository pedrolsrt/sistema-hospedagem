# Sistema de Hospedagem

Projeto desenvolvido para a disciplina de Programação Modular com o objetivo de implementar um sistema de gerenciamento de hospedagens, utilizando arquitetura em camadas, API REST com Spring Boot e persistência em banco de dados MySQL.

## Objetivo do projeto

O sistema foi construído para auxiliar no gerenciamento de residências e quartos disponíveis para hospedagem, permitindo o cadastro de clientes, a realização de reservas e aluguéis, o controle de disponibilidade, o cálculo automático de diárias, o controle de pagamentos e a emissão de recibos.

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Lombok
- Bean Validation

## Estrutura do projeto

O sistema foi organizado seguindo o padrão de arquitetura em camadas:

- **Controller**: responsável pelos endpoints da API
- **Service**: responsável pelas regras de negócio
- **Repository**: responsável pelo acesso ao banco de dados
- **Model**: entidades do sistema
- **DTO**: objetos utilizados para entrada e saída de dados
- **Exception**: tratamento global de erros

## Funcionalidades implementadas

### Clientes
- cadastro de clientes
- listagem de clientes
- busca por id
- atualização de dados
- exclusão de clientes

### Residências
- cadastro de residências
- listagem de residências
- busca por id
- atualização de dados
- exclusão de residências

### Quartos
- cadastro de quartos
- listagem de quartos
- busca por id
- atualização de dados
- exclusão de quartos

### Reservas
- cadastro de reservas futuras
- validação de conflito de período
- listagem de reservas
- busca por id
- cancelamento de reservas

### Aluguéis
- cadastro de aluguéis
- validação de disponibilidade do quarto
- cálculo automático de diárias
- cálculo do valor final
- criação de pagamento associado
- emissão de recibo
- listagem de aluguéis
- busca por id
- marcação de pagamento como pago

## Regras de negócio aplicadas

- o sistema impede conflito de reservas no mesmo quarto para períodos incompatíveis
- o sistema impede aluguéis em quartos já ocupados
- a quantidade de diárias é calculada considerando a regra do meio-dia
- o valor final do aluguel é calculado com base no valor da diária e nos adicionais do quarto
- cada aluguel gera um pagamento associado
- o recibo do aluguel é gerado com os dados principais da hospedagem

## Como executar o projeto

### 1. Clonar o repositório
```bash
git clone <url-do-repositorio>