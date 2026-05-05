# Sistema de Hospedagem API

API REST desenvolvida em Java com Spring Boot para gerenciamento de hospedagens em residências. O sistema permite o controle completo de clientes, residências, quartos, reservas e aluguéis, incluindo regras de negócio realistas, cálculo automático de diárias e geração de recibos.

---

## Objetivo

O projeto tem como objetivo aplicar os conceitos de Programação Modular, com foco em organização, separação de responsabilidades, reutilização de código e implementação de regras de negócio consistentes.

---

## Tecnologias Utilizadas

- Java 17  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Bean Validation  
- MySQL  
- Maven  
- Lombok  
- JUnit 5  
- Mockito  

---

## Arquitetura do Sistema

O sistema foi desenvolvido seguindo o padrão de **arquitetura em camadas**, garantindo organização, manutenção e escalabilidade.

### Camadas

- **Controller** → Responsável pela exposição dos endpoints REST  
- **Service** → Contém as regras de negócio do sistema  
- **Repository** → Acesso e manipulação de dados no banco  
- **Model** → Representação das entidades  
- **DTO** → Transporte de dados entre camadas  
- **Exception** → Tratamento centralizado de erros  
- **Enums** → Tipos e status do sistema  

### Estrutura de Pacotes

```
src/main/java/br/pucminas/sistema_hospedagem
├── controller
├── dto
├── enums
├── exception
├── model
├── repository
└── service
```

### Fluxo da Aplicação

Controller → Service → Repository → Banco de Dados

---

## Funcionalidades do Sistema

### Clientes
- Cadastro de clientes  
- Listagem de clientes  
- Busca por id  
- Atualização cadastral  
- Exclusão  
- Autenticação por e-mail e senha  

---

### Residências
- Cadastro de residências  
- Listagem  
- Busca por id  
- Atualização  
- Exclusão  
- Consulta de histórico de hospedagens  

---

### Quartos
- Cadastro de quartos  
- Listagem  
- Busca por id  
- Atualização  
- Exclusão  

#### Tipos de Quarto

O sistema suporta três tipos principais de quartos:

- **INDIVIDUAL**
  - Permite uma ou mais camas de solteiro  
  - Não permite berço  
  - Valor baseado na quantidade de camas  
  - Limite de hóspedes proporcional às camas  

- **CASAL**
  - Possui cama casal, queen ou king  
  - Pode incluir berço opcional  
  - Possui adicionais por tipo de cama  
  - Pode incluir taxa extra por conforto  

- **FAMILIA**
  - Capacidade ampliada  
  - Permite múltiplos ambientes  
  - Cálculo baseado na quantidade de hóspedes  
  - Permite desconto progressivo para grupos  

---

### Reservas
- Criação de reservas  
- Cancelamento de reservas  
- Verificação de conflito de datas  
- Bloqueio de reservas em quartos ocupados  

---

### Aluguéis
- Criação de aluguel  
- Verificação de disponibilidade do quarto  
- Validação de capacidade máxima  
- Controle de pagamento  
- Cálculo automático de diárias  
- Geração de recibo  
- Histórico de aluguéis por residência  

---

## Regras de Negócio

O sistema implementa diversas regras para garantir consistência:

- Não permite reservas com datas conflitantes  
- Não permite reservar quartos já alugados  
- Não permite aluguel em quarto ocupado  
- Não permite aluguel com reserva ativa no mesmo período  
- Valida capacidade máxima do quarto  
- Valida solicitação de berço conforme o tipo do quarto  
- Aplica regra do meio-dia no cálculo de diárias  
- Calcula o valor final considerando:
  - tipo de quarto  
  - número de hóspedes  
  - tipo de cama  
  - adicionais (ar condicionado, hidromassagem, berço)  
- Aplica desconto progressivo para quartos família  
- Todo aluguel gera automaticamente um pagamento associado  

---

## Como Executar o Projeto

### 1. Clonar o repositório

```
git clone URL_DO_REPOSITORIO
```

### 2. Acessar a pasta

```
cd sistema-hospedagem
```

### 3. Criar o banco de dados

```
CREATE DATABASE sistema_hospedagem;
```

### 4. Configurar o arquivo application.properties

```
spring.datasource.url=jdbc:mysql://localhost:3306/sistema_hospedagem
spring.datasource.username=root
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

### 5. Executar a aplicação

```
.\mvnw.cmd spring-boot:run
```

### 6. Executar testes

```
.\mvnw.cmd clean test
```

---

## Base da API

```
http://localhost:8080
```

---

## Endpoints Principais

### Clientes
POST /clientes  
POST /clientes/login  
GET /clientes  
GET /clientes/{id}  
PUT /clientes/{id}  
DELETE /clientes/{id}  

---

### Residências
POST /residencias  
GET /residencias  
GET /residencias/{id}  
PUT /residencias/{id}  
DELETE /residencias/{id}  
GET /residencias/{id}/historico  

---

### Quartos
POST /quartos  
GET /quartos  
GET /quartos/{id}  
PUT /quartos/{id}  
DELETE /quartos/{id}  

---

### Reservas
POST /reservas  
GET /reservas  
GET /reservas/{id}  
PATCH /reservas/{id}/cancelar  

---

### Aluguéis
POST /alugueis  
GET /alugueis  
GET /alugueis/{id}  
PATCH /alugueis/{id}/pagar  
GET /alugueis/{id}/recibo  

---

## Exemplos de Requisição

### Cliente

```json
{
  "nome": "Pedro Lucas",
  "cpf": "12345678901",
  "endereco": "Rua A, 100",
  "telefone": "31999999999",
  "email": "pedro@email.com",
  "senha": "123456"
}
```

---

### Quarto

```json
{
  "tipo": "CASAL",
  "valorBaseDiaria": 150.0,
  "possuiArCondicionado": true,
  "possuiHidromassagem": true,
  "quantidadeCamasSolteiro": 0,
  "quantidadeCamasCasal": 0,
  "quantidadeCamasQueen": 1,
  "quantidadeCamasKing": 0,
  "capacidadeMaxima": 2,
  "quantidadeAmbientes": 1,
  "permiteBerco": true,
  "residenciaId": 1
}
```

---

### Aluguel

```json
{
  "clienteId": 1,
  "quartoId": 1,
  "dataEntrada": "2026-04-28T13:00:00",
  "dataSaida": "2026-04-29T13:30:00",
  "numeroHospedes": 2,
  "solicitaBerco": false
}
```

---

## Testes Automatizados

O projeto possui testes cobrindo cenários críticos:

- CPF duplicado  
- Login inválido  
- Reserva com conflito  
- Quarto ocupado  
- Validação de datas  
- Cálculo de diárias  
- Aplicação de regras de negócio  

---

## Boas Práticas Aplicadas

- Arquitetura em camadas  
- DTO Pattern  
- Repository Pattern  
- Service Layer  
- Bean Validation  
- Tratamento global de exceções  
- Separação de responsabilidades  
- Código limpo e organizado  

---

## Evoluções Futuras

- Autenticação com JWT  
- Criptografia de senhas  
- Dashboard administrativo  
- Relatórios financeiros  
- Upload de imagens  

---

## Autor

Pedro Lucas Soares Rezende  
Raquel Cristina Pereira dos Santos  
Estudantes de Engenharia de Software - PUC Minas