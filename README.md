# FarmaCheck - API de Gestao de Medicamentos em Farmacias Publicas

Sistema para verificacao de disponibilidade de medicamentos em farmacias publicas, com notificacao automatica via Kafka e email quando medicamentos indisponiveis ficam disponiveis.

---

## Objetivo do Projeto

O FarmaCheck permite que pacientes consultem a disponibilidade de medicamentos em farmacias publicas. Quando um medicamento nao esta disponivel, o sistema registra a pesquisa em uma fila Kafka e verifica periodicamente se o medicamento ficou disponivel, notificando o usuario por email.

Principais funcionalidades:
- Cadastro e gerenciamento de medicamentos (administrador)
- Verificacao de disponibilidade de medicamentos no estoque
- Fila Kafka para medicamentos indisponiveis
- Reprocessamento periodico da fila
- Notificacao automatica por email quando o medicamento fica disponivel
- Autenticacao e autorizacao via JWT
- Gestao de unidades de saude, estoque e movimentacoes

---

## Arquitetura Geral

```
Cliente (Browser / Swagger / HTTP)
        |
        v
Spring Boot API (Java 21) -- porta 8090
        |
        +---> PostgreSQL (dados)
        |
        +---> Apache Kafka (mensageria)
        |         |
        |         +---> Consumer (processa fila)
        |         +---> Scheduler (reprocessamento periodico)
        |
        +---> SMTP Server (envio de emails)
```

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Data JPA / Hibernate**
- **Spring Security + JWT** (auth0 java-jwt)
- **Spring Kafka** (producer, consumer, scheduler)
- **Spring Mail** (notificacoes por email)
- **PostgreSQL 17** (banco de dados)
- **Apache Kafka + Zookeeper** (mensageria)
- **Flyway** (versionamento de banco)
- **Lombok** (reducao de boilerplate)
- **MapStruct** (mapeamento DTO <-> Entity)
- **SpringDoc OpenAPI / Swagger UI** (documentacao interativa)
- **Docker / Docker Compose** (infraestrutura local)
- **Maven** (build e dependencias)
- **HikariCP** (pool de conexoes)

---

## Modelo de Dados (Entidades)

### Usuario
| Campo         | Tipo          | Descricao                          |
|---------------|---------------|------------------------------------|
| id            | int           | Identificador unico (sequence)     |
| nome          | String        | Nome do usuario                    |
| email         | String        | Email (unico)                      |
| senha         | String        | Senha criptografada (BCrypt)       |
| tipoUsuario   | TipoUsuario   | `PACIENTE` ou `ADMINISTRADOR`      |
| unidade       | Unidade       | Unidade de saude vinculada         |
| criacao       | LocalDateTime | Data de criacao (auditoria)        |
| ultimaModificacao | LocalDateTime | Ultima modificacao (auditoria) |

### Medicamento
| Campo          | Tipo          | Descricao                          |
|----------------|---------------|------------------------------------|
| id             | int           | Identificador unico (sequence)     |
| nome           | String        | Nome do medicamento                |
| pricipioAtivo  | String        | Principio ativo                    |
| dosagem        | String        | Dosagem                            |
| laboratorio    | String        | Laboratorio fabricante             |
| criacao        | LocalDateTime | Data de criacao (auditoria)        |
| ultimaModificacao | LocalDateTime | Ultima modificacao (auditoria) |

### Unidade
| Campo        | Tipo         | Descricao                           |
|--------------|--------------|-------------------------------------|
| id           | int          | Identificador unico (sequence)      |
| nome         | String       | Nome da unidade de saude            |
| cep          | String       | CEP                                 |
| telefone     | String       | Telefone                            |
| email        | String       | Email                               |
| tipoUnidade  | TipoUnidade  | `UPA` ou `UBS`                      |

### Estoque
| Campo        | Tipo         | Descricao                           |
|--------------|--------------|-------------------------------------|
| id           | int          | Identificador unico (sequence)      |
| quantidade   | Integer      | Quantidade disponivel               |
| unidade      | Unidade      | Unidade de saude                    |
| medicamento  | Medicamento  | Medicamento em estoque              |

### Movimentacao
| Campo             | Tipo              | Descricao                        |
|-------------------|-------------------|----------------------------------|
| id                | int               | Identificador unico (sequence)   |
| quantidade        | Integer           | Quantidade movimentada           |
| tipoMovimentacao  | TipoMovimentacao  | `ENTRADA`, `SAIDA` ou `AJUSTE`   |
| usuario           | Usuario           | Usuario responsavel              |
| estoque           | Estoque           | Estoque afetado                  |

---

## Enums

| Enum              | Valores                          |
|-------------------|----------------------------------|
| TipoUsuario       | `PACIENTE`, `ADMINISTRADOR`      |
| TipoUnidade       | `UPA`, `UBS`                     |
| TipoMovimentacao  | `ENTRADA`, `SAIDA`, `AJUSTE`     |

---

## Endpoints da API

### Autenticacao (publico - sem token)

| Metodo | Endpoint             | Descricao                    | Body                                                                                       |
|--------|----------------------|------------------------------|---------------------------------------------------------------------------------------------|
| POST   | `/api/auth/sign-up`  | Registrar novo usuario       | `{"nome":"...","email":"...","senha":"...","tipoUsuario":"ADMINISTRADOR","idUnidade":1}`    |
| POST   | `/api/auth/sign-in`  | Fazer login e obter token    | `{"email":"...","senha":"..."}`                                                             |

**Resposta do sign-in:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs..."
}
```

### Medicamentos - CRUD (requer role `ADMINISTRADOR`)

Adicione o header `Authorization: Bearer <token>` em todas as requisicoes abaixo.

| Metodo | Endpoint               | Descricao                  | Body                                                                                 |
|--------|------------------------|----------------------------|---------------------------------------------------------------------------------------|
| POST   | `/api/medicamentos`    | Cadastrar medicamento      | `{"nome":"Paracetamol","pricipioAtivo":"Paracetamol","dosagem":"500mg","laboratorio":"EMS"}` |
| GET    | `/api/medicamentos`    | Listar todos medicamentos  | -                                                                                     |
| GET    | `/api/medicamentos/{id}` | Buscar por ID            | -                                                                                     |
| PUT    | `/api/medicamentos/{id}` | Atualizar medicamento    | `{"nome":"...","pricipioAtivo":"...","dosagem":"...","laboratorio":"..."}`             |
| DELETE | `/api/medicamentos/{id}` | Deletar medicamento      | -                                                                                     |

### Verificacao de Disponibilidade (requer autenticacao - qualquer role)

| Metodo | Endpoint                             | Descricao                                      | Body                                   |
|--------|--------------------------------------|-------------------------------------------------|----------------------------------------|
| POST   | `/api/medicamentos/disponibilidade`  | Verificar se medicamento esta disponivel        | `{"nomeMedicamento":"Paracetamol"}`    |

**Resposta - Medicamento disponivel:**
```json
{
  "nomeMedicamento": "Paracetamol",
  "disponivel": true,
  "mensagem": "Medicamento disponível na farmácia pública"
}
```

**Resposta - Medicamento indisponivel (enviado para fila Kafka):**
```json
{
  "nomeMedicamento": "Paracetamol",
  "disponivel": false,
  "mensagem": "Medicamento não disponível. Sua pesquisa foi registrada e você será notificado quando estiver disponível."
}
```

### Endpoints de Teste (autenticados)

| Metodo | Endpoint                    | Descricao                                | Role necessaria  |
|--------|-----------------------------|------------------------------------------|------------------|
| GET    | `/api/test`                 | Testar autenticacao (qualquer role)      | Qualquer         |
| GET    | `/api/test/administrador`   | Testar autenticacao como administrador   | ADMINISTRADOR    |
| GET    | `/api/test/paciente`        | Testar autenticacao como paciente        | PACIENTE         |

---

## Fluxo de Verificacao de Disponibilidade

```
1. Usuario autenticado faz POST /api/medicamentos/disponibilidade
   com { "nomeMedicamento": "Paracetamol" }
                    |
                    v
2. Sistema busca no Estoque por medicamento com esse nome e quantidade > 0
                    |
        +-----------+-----------+
        |                       |
        v                       v
   DISPONIVEL              INDISPONIVEL
        |                       |
        v                       v
3a. Retorna resposta       3b. Envia evento para topico Kafka
    { disponivel: true }       "medicamento-indisponivel"
                                com nome do medicamento,
                                email e nome do usuario
                                |
                                v
                         4. Kafka Consumer recebe evento
                                |
                                v
                         5. Verifica novamente no Estoque
                                |
                        +-------+-------+
                        |               |
                        v               v
                   DISPONIVEL      INDISPONIVEL
                        |               |
                        v               v
                   6a. Envia email  6b. Log: ainda
                       para o          indisponivel
                       usuario
```

---

## Fluxo de Reprocessamento Periodico (Scheduler)

```
1. A cada 60 segundos (configuravel), o Scheduler executa
                    |
                    v
2. Cria um KafkaConsumer temporario (grupo: farmacheck-reprocessamento-group)
                    |
                    v
3. Faz poll no topico "medicamento-indisponivel" (timeout 5s)
                    |
        +-----------+-----------+
        |                       |
        v                       v
   FILA VAZIA              MENSAGENS ENCONTRADAS
        |                       |
        v                       v
   Log: nada a           4. Para cada mensagem, verifica Estoque
   reprocessar                  |
                        +-------+-------+
                        |               |
                        v               v
                   DISPONIVEL      INDISPONIVEL
                        |               |
                        v               v
                   5a. Envia email  5b. Log: ainda
                       para o          indisponivel
                       usuario
```

---

## Fluxo de Notificacao por Email

Quando o Consumer ou o Scheduler detecta que um medicamento ficou disponivel:

1. O `EmailService` cria uma mensagem `SimpleMailMessage`
2. Define destinatario (email do usuario), assunto e corpo
3. Envia via `JavaMailSender` (Spring Mail)
4. Em ambiente local, os emails sao capturados pelo Fake SMTP Server (porta 1080)

**Exemplo de email enviado:**
```
Para: paciente@email.com
Assunto: FarmaCheck - Medicamento Disponível: Paracetamol

Olá João,

O medicamento que você pesquisou está agora disponível na farmácia pública!

Medicamento: Paracetamol

Dirija-se à unidade mais próxima para retirar o seu medicamento.

Atenciosamente,
Equipe FarmaCheck
```

---

## Fluxo de Autenticacao (JWT)

```
1. POST /api/auth/sign-up  -->  Cria usuario com senha criptografada (BCrypt)
2. POST /api/auth/sign-in  -->  Valida credenciais e retorna token JWT
3. Requisicoes autenticadas:
   Header: Authorization: Bearer <token>
   --> UserAuthenticationFilter extrai e valida o token
   --> Carrega o usuario e define as authorities (PACIENTE / ADMINISTRADOR)
   --> SecurityConfiguration verifica se o endpoint requer role especifica
```

- Token expira em 2 horas
- Algoritmo: HMAC256
- Secret configuravel via `api.security.token.secret`

---

## Seguranca dos Endpoints

| Endpoint                               | Acesso                            |
|----------------------------------------|-----------------------------------|
| `/api/auth/sign-up`                    | Publico                           |
| `/api/auth/sign-in`                    | Publico                           |
| `/swagger-ui/**`, `/v3/api-docs/**`    | Publico                           |
| `/api/medicamentos/disponibilidade`    | Autenticado (qualquer role)       |
| `/api/test`                            | Autenticado (qualquer role)       |
| `/api/medicamentos` (CRUD)             | ADMINISTRADOR                     |
| `/api/test/administrador`              | ADMINISTRADOR                     |
| `/api/test/paciente`                   | PACIENTE                          |

---

## Estrutura do Projeto

```
src/main/java/com/fiap/farmacheck
|
+-- FarmaCheckApplication.java              # Classe principal
|
+-- config/
|   +-- KafkaConfig.java                    # Configuracao do topico Kafka
|   +-- SwaggerConfig.java                  # Configuracao do Swagger/OpenAPI
|
+-- controller/
|   +-- AuthController.java                 # Endpoints de autenticacao
|   +-- MedicamentoController.java          # CRUD + verificacao de disponibilidade
|
+-- service/
|   +-- AuthService.java                    # Registro e autenticacao de usuarios
|   +-- JwtTokenService.java               # Geracao e validacao de tokens JWT
|   +-- MedicamentoService.java            # Logica de negocio de medicamentos
|   +-- EmailService.java                  # Envio de emails de notificacao
|
+-- kafka/
|   +-- MedicamentoIndisponivelProducer.java    # Envia eventos para Kafka
|   +-- MedicamentoIndisponivelConsumer.java    # Consome eventos e notifica
|   +-- MedicamentoReprocessamentoScheduler.java # Reprocessamento periodico
|
+-- repository/
|   +-- MedicamentoRepository.java          # Acesso a dados de medicamentos
|   +-- EstoqueRepository.java              # Acesso a dados de estoque
|   +-- UsuarioRepository.java              # Acesso a dados de usuarios
|   +-- UnidadeRepository.java              # Acesso a dados de unidades
|   +-- MovimentacaoRepository.java         # Acesso a dados de movimentacoes
|
+-- model/
|   +-- entity/
|   |   +-- Auditoria.java                 # Classe base (criacao/ultimaModificacao)
|   |   +-- Medicamento.java
|   |   +-- Estoque.java
|   |   +-- Usuario.java
|   |   +-- Unidade.java
|   |   +-- Movimentacao.java
|   |
|   +-- dto/
|   |   +-- medicamento/                   # MedicamentoRequestDTO, MedicamentoResponseDTO
|   |   +-- disponibilidade/               # DisponibilidadeRequestDTO, DisponibilidadeResponseDTO, MedicamentoIndisponivelEvent
|   |   +-- usuario/                       # UsuarioRequestDTO, UsuarioLoginRequestDTO, UsuarioResponseDTO
|   |   +-- estoque/                       # EstoqueRequestDTO, EstoqueResponseDTO
|   |   +-- unidade/                       # UnidadeRequestDTO, UnidadeResponseDTO
|   |   +-- movimentacao/                  # MovimentacaoRequestDTO, MovimentacaoResponseDTO
|   |   +-- token/                         # TokenResponseDTO
|   |   +-- auditoria/                     # AuditoriaResponseDTO
|   |
|   +-- enums/
|       +-- TipoUsuario.java               # PACIENTE, ADMINISTRADOR
|       +-- TipoUnidade.java               # UPA, UBS
|       +-- TipoMovimentacao.java          # ENTRADA, SAIDA, AJUSTE
|
+-- mapper/                                 # MapStruct mappers (Entity <-> DTO)
|   +-- MedicamentoMapper.java
|   +-- UsuarioMapper.java
|   +-- EstoqueMapper.java
|   +-- UnidadeMapper.java
|   +-- MovimentacaoMapper.java
|   +-- AuditoriaMapper.java
|   +-- *MapperHelper.java                 # Helpers para resolver IDs
|
+-- exception/
|   +-- GlobalExceptionHandler.java         # Tratamento global de erros
|   +-- ErrorResponse.java                  # Modelo padrao de erro
|   +-- ResourceNotFoundException.java
|   +-- EmailAlreadyExistsException.java
|   +-- GenerateTokenErrorException.java
|   +-- ValidateTokenErrorException.java
|
+-- security/
    +-- jwt/
    |   +-- SecurityConfiguration.java      # Configuracao do Spring Security
    |   +-- UserAuthenticationFilter.java   # Filtro JWT
    +-- service/
        +-- UserDetailsImpl.java            # Implementacao de UserDetails
```

---

## Infraestrutura (Docker Compose)

O `docker-compose.yaml` sobe toda a infraestrutura necessaria:

| Servico        | Imagem                          | Porta     | Descricao                       |
|----------------|---------------------------------|-----------|---------------------------------|
| database       | postgres:17                     | 5433:5432 | Banco de dados PostgreSQL       |
| pgadmin        | dpage/pgadmin4                  | 5050:80   | Interface visual do banco       |
| zookeeper      | confluentinc/cp-zookeeper:7.5.0 | 2181      | Coordenador do Kafka           |
| kafka          | confluentinc/cp-kafka:7.5.0    | 9092      | Broker de mensageria            |
| smtp-server    | haravich/fake-smtp-server       | 1080/1025 | Servidor SMTP fake (testes)     |

---

## Configuracao (Variaveis de Ambiente)

As seguintes variaveis podem ser configuradas (valores padrao entre parenteses):

| Variavel                          | Descricao                           | Padrao                           |
|-----------------------------------|-------------------------------------|----------------------------------|
| `DB_PORT`                         | Porta do PostgreSQL                 | -                                |
| `DB_USERNAME`                     | Usuario do banco                    | admin                            |
| `DB_PASSWORD`                     | Senha do banco                      | admin                            |
| `DB_NAME`                         | Nome do banco                       | farmadb                          |
| `JWT_SECRET`                      | Secret para gerar tokens JWT        | segredo-padrao-apenas-para-dev   |
| `JWT_EXPIRATION`                  | Tempo de expiracao do token (ms)    | 86400000                         |
| `KAFKA_BOOTSTRAP_SERVERS`         | Endereco do broker Kafka            | localhost:9092                   |
| `KAFKA_REPROCESSAMENTO_INTERVALO` | Intervalo do scheduler (ms)         | 60000                            |
| `MAIL_HOST`                       | Host SMTP                           | localhost                        |
| `MAIL_PORT`                       | Porta SMTP                          | 1025                             |
| `SHOW_SQL`                        | Exibir SQL no log                   | false                            |

---

## Como Executar

### Pre-requisitos
- Java 21
- Docker e Docker Compose
- Maven (ou usar o wrapper `./mvnw`)

### 1. Subir a infraestrutura

```bash
docker-compose up -d
```

Isso inicia PostgreSQL, PGAdmin, Kafka, Zookeeper e o servidor SMTP fake.

### 2. Compilar e executar a aplicacao

```bash
./mvnw clean package -DskipTests
java -jar target/farmacheck-0.0.1-SNAPSHOT.jar
```

Ou com o Maven diretamente:

```bash
./mvnw spring-boot:run
```

A aplicacao inicia na porta **8090**.

### 3. Acessar a documentacao

- **Swagger UI:** http://localhost:8090/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8090/v3/api-docs
- **PGAdmin:** http://localhost:5050
- **Fake SMTP (emails):** http://localhost:1080

---

## Exemplo de Uso Completo

### 1. Registrar um administrador
```bash
curl -X POST http://localhost:8090/api/auth/sign-up \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Admin Farmacia",
    "email": "admin@farmacia.com",
    "senha": "admin123",
    "tipoUsuario": "ADMINISTRADOR",
    "idUnidade": 1
  }'
```

### 2. Fazer login
```bash
curl -X POST http://localhost:8090/api/auth/sign-in \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@farmacia.com", "senha": "admin123"}'
```

Resposta:
```json
{"token": "eyJhbGciOiJIUzI1NiIs..."}
```

### 3. Cadastrar um medicamento (admin)
```bash
curl -X POST http://localhost:8090/api/medicamentos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "nome": "Paracetamol",
    "pricipioAtivo": "Paracetamol",
    "dosagem": "500mg",
    "laboratorio": "EMS"
  }'
```

### 4. Verificar disponibilidade (qualquer usuario autenticado)
```bash
curl -X POST http://localhost:8090/api/medicamentos/disponibilidade \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"nomeMedicamento": "Paracetamol"}'
```

Se indisponivel, o evento e enviado para o Kafka e o usuario recebera um email quando o medicamento ficar disponivel.

### 5. Verificar emails enviados
Acesse http://localhost:1080 para ver os emails capturados pelo servidor SMTP fake.

---

## Topico Kafka

| Topico                       | Descricao                                                  |
|------------------------------|------------------------------------------------------------|
| `medicamento-indisponivel`   | Eventos de medicamentos pesquisados que nao estao em estoque |

**Estrutura do evento:**
```json
{
  "nomeMedicamento": "Paracetamol",
  "emailUsuario": "paciente@email.com",
  "nomeUsuario": "Joao Silva",
  "dataPesquisa": "2025-01-15T10:30:00"
}
```

**Consumer groups:**
- `farmacheck-group` - Consumer principal (`@KafkaListener`)
- `farmacheck-reprocessamento-group` - Scheduler de reprocessamento periodico

---

## Build e Deploy

### Build Local
```bash
./mvnw clean package -DskipTests
```

### Build com Docker
```bash
docker build -t farmacheck .
docker run -p 8090:8090 farmacheck
```

---

## Projeto Academico

Projeto desenvolvido para fins academicos - FIAP - Fase 5 (Arquitetura e Desenvolvimento Java), com foco em:
- Arquitetura de microsservicos e mensageria
- Integracao com Apache Kafka
- Seguranca com JWT
- Notificacoes assincronas
- Spring Boot e boas praticas de desenvolvimento

## Autores
- Francisco Aurizelio de Sousa ([GitHub](https://github.com/faurizel))
- Lucas Herculano Amaro ([GitHub](https://github.com/LucasHerculanoAmaro))
- Matheus Jesus de Souza ([GitHub](https://github.com/offteuz))
- Matteus Santos de Abreu ([GitHub](https://github.com/Nexusf1re))
- Vitor Silva Franca ([GitHub](https://github.com/vitor-silva-franca))
