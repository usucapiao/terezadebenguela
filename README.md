# Instituto Tereza de Benguela — Portal Web

![Java](https://img.shields.io/badge/Java-25-blue?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Produção-4169E1?logo=postgresql&logoColor=white)
![H2](https://img.shields.io/badge/H2-Desenvolvimento-003545)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-Deploy-46E3B7?logo=render&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9.14-C71A36?logo=apachemaven&logoColor=white)
![Status](https://img.shields.io/badge/Status-Concluído-success)

Portal web completo do **Instituto Tereza de Benguela**, organização dedicada à preservação da cultura quilombola de **Vila Bela da Santíssima Trindade – MT**. O sistema combina uma API RESTful em Java com um frontend estático, além de um painel administrativo (CMS) para gestão de todo o conteúdo institucional.

---

## Sumário

- [Sobre o Instituto](#sobre-o-instituto)
- [Funcionalidades](#funcionalidades)
- [Arquitetura do Sistema](#arquitetura-do-sistema)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura de Diretórios](#estrutura-de-diretórios)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Como Executar](#como-executar)
  - [Desenvolvimento local (H2)](#desenvolvimento-local-h2)
  - [Com Docker Compose (recomendado para produção)](#com-docker-compose-recomendado-para-produção)
  - [JAR direto (sem Docker)](#jar-direto-sem-docker)
- [Acesso ao Sistema](#acesso-ao-sistema)
- [API REST — Referência de Endpoints](#api-rest--referência-de-endpoints)
- [Modelo de Dados](#modelo-de-dados)
- [Segurança e Autenticação](#segurança-e-autenticação)
- [Upload de Mídias](#upload-de-mídias)
- [Painel Administrativo (CMS)](#painel-administrativo-cms)
- [Testes](#testes)
- [Banco de Dados](#banco-de-dados)
- [Migração para Produção](#migração-para-produção)
  - [Opção A — Docker Compose](#opção-a--docker-compose-recomendada)
  - [Opção B — JAR direto](#opção-b--jar-direto-no-servidor)
  - [Opção C — Render (PaaS)](#opção-c--render-paas)

---

## Sobre o Instituto

O **Instituto Tereza de Benguela** é uma organização cultural com sede em Vila Bela da Santíssima Trindade, primeira capital de Mato Grosso. Sua missão é preservar, valorizar e fomentar a rica herança cultural quilombola da região — uma identidade forjada ao longo de mais de 100 anos de isolamento e resistência do povo negro.

Os projetos principais gerenciados pelo portal incluem:

| Projeto | Descrição |
|---|---|
| **Festança do Congo** | Testemunho vivo da resistência afro-brasileira, com a Dança do Congo e o Chorado — patrimônio imaterial em devoção a São Benedito |
| **Festival de Praia** | Evento anual (set/out) que movimenta turismo e apoia grupos culturais sem financiamento municipal |
| **Dia da Consciência Negra** | Celebração de 20 de novembro com palestras, ações culturais e valorização da identidade afro-brasileira |

A discografia do portal reúne faixas do **Quilombo Aurora do Quariterê**, grupo musical que preserva os cantos e ritmos tradicionais da comunidade.

---

## Funcionalidades

### Portal Público
- Página inicial com carrossel de projetos, galeria de notícias e player de discografia
- Listagem completa de projetos com detalhes, imagens e características
- Feed de notícias com suporte a vídeos do YouTube e imagens
- Player de áudio para discografia quilombola
- Formulário de cadastro de voluntários
- Conteúdo da página de voluntariado editável via CMS

### Painel Administrativo
- Autenticação segura com JWT
- CRUD completo de projetos, notícias, membros da diretoria e discografia
- Gerenciamento de candidaturas de voluntários (visualização de mensagens de motivação)
- Editor de configurações globais do site (textos, contatos, redes sociais)
- Upload de imagens (JPG, PNG, GIF, WEBP) e áudios (MP3, OGG, WAV, AAC, FLAC)
- Seletor de ícones Font Awesome integrado

---

## Arquitetura do Sistema

O projeto segue o padrão **Mono-repositório Servidor Incorporado**: o Spring Boot serve tanto a API REST quanto os arquivos estáticos do frontend a partir do mesmo processo.

```
Navegador (Cliente)
       │
       ├── GET /*, /*.html, /scripts/**, /styles/**, /assets/**
       │         └── Recursos estáticos servidos pelo Spring Boot
       │
       └── /api/**  (JSON)
                 └── Controllers → Services → Repositories → Banco de Dados
```

### Camadas do Backend

```
br.com.instituto.teresa/
├── controller/     ← Roteamento HTTP, validação de DTOs (@Valid), respostas JSON
├── service/        ← Regras de negócio, orquestração, lógica interna
├── repository/     ← Interfaces Spring Data JPA (sem SQL manual)
├── domain/         ← Entidades JPA (@Entity), mapeamento objeto-relacional
├── dto/            ← Objetos de transferência (Request/Response), isolam a API da entidade
├── config/         ← SecurityConfig, SecurityFilter, WebConfig, DataSeeder
└── exception/      ← GlobalExceptionHandler (@RestControllerAdvice)
```

### Padrão de Segurança — Fluxo JWT

```
POST /api/auth/login
   { username, password }
         │
   AuthController → AuthenticationManager (valida BCrypt)
         │
   TokenService → gera JWT assinado (HMAC256, 2h de validade)
         │
   └── Retorna: { token: "eyJ..." }

Requisições subsequentes (rotas protegidas):
   Authorization: Bearer eyJ...
         │
   SecurityFilter (OncePerRequestFilter)
         │
   TokenService → valida assinatura e extrai subject
         │
   SecurityContextHolder → injeta usuário autenticado na thread
```

---

## Tecnologias Utilizadas

### Backend
| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | **25** | Linguagem principal |
| Spring Boot | **3.5.14** | Framework web e DI |
| Spring Data JPA | (Boot) | Abstração do banco de dados |
| Spring Security | (Boot) | Autenticação e autorização |
| Spring Validation | (Boot) | Validação de DTOs com Jakarta Bean Validation |
| Auth0 Java JWT | 4.4.0 | Geração e validação de tokens JWT |
| H2 Database | (Boot) | Banco em arquivo para desenvolvimento |
| PostgreSQL | (Boot) | Banco relacional para produção |
| Maven Wrapper | 3.9.14 | Gerenciamento de dependências e build |
| JUnit 5 + MockMvc | (Boot Test) | Testes de integração dos controllers |

> **Lombok não é utilizado.** Todas as entidades foram escritas em Java explícito com getters, setters e construtores manuais — decisão de projeto para transparência do código.

### Frontend
| Tecnologia | Finalidade |
|---|---|
| HTML5 / CSS3 | Estrutura e estilo das páginas |
| JavaScript (Vanilla ES6+) | Lógica assíncrona, consumo da API via `fetch()` |
| Tailwind CSS (CDN) | Utilitários de estilo |
| Font Awesome (CDN) | Iconografia |

### Infraestrutura
| Tecnologia | Finalidade |
|---|---|
| Docker | Containerização da aplicação (imagem `eclipse-temurin:25-jre-alpine`) |
| Docker Compose | Orquestração de app + PostgreSQL |
| PostgreSQL 17 | Banco de dados em produção/Docker |

---

## Estrutura de Diretórios

```
.
├── src/
│   ├── main/
│   │   ├── java/br/com/instituto/teresa/
│   │   │   ├── TeresaApplication.java          # Ponto de entrada
│   │   │   ├── config/
│   │   │   │   ├── DataSeeder.java             # Seed inicial do banco
│   │   │   │   ├── SecurityConfig.java         # Filtros e rotas públicas/privadas
│   │   │   │   ├── SecurityFilter.java         # Interceptador JWT
│   │   │   │   └── WebConfig.java              # CORS e recursos estáticos
│   │   │   ├── controller/                     # AuthController, ProjectController, ...
│   │   │   ├── service/                        # TokenService, ProjectService, ...
│   │   │   ├── repository/                     # Interfaces JpaRepository
│   │   │   ├── domain/                         # Entidades JPA
│   │   │   ├── dto/                            # Records Request/Response
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/                         # Frontend servido pelo Spring
│   │           ├── index.html                  # Página inicial
│   │           ├── projetos.html               # Listagem de projetos
│   │           ├── noticias.html               # Feed de notícias
│   │           ├── voluntario.html             # Cadastro de voluntários
│   │           ├── scripts/                    # JS público (news, projects, discography)
│   │           ├── styles/                     # CSS customizado
│   │           ├── assets/                     # Imagens e MP3s da discografia
│   │           └── admin/                      # SPA do painel administrativo
│   │               ├── login.html
│   │               ├── index.html (dashboard)
│   │               ├── projects.html
│   │               ├── news.html
│   │               ├── board.html
│   │               ├── volunteers.html
│   │               ├── discography.html
│   │               ├── settings.html
│   │               └── js/                     # Módulos JS do admin
│   └── test/java/br/com/instituto/teresa/
│       └── controller/                         # Testes de integração MockMvc
├── data/                                       # Arquivo H2 (gerado em runtime)
├── uploads/                                    # Mídias enviadas pelo admin (gerado em runtime)
├── Dockerfile                                  # Build multi-stage (Maven 3.9 + JRE 25 Alpine)
├── Dockerfile.render                           # Variante para deploy no Render (PaaS)
├── docker-compose.yml                          # Orquestração app + PostgreSQL 17
├── .dockerignore
├── .env.example                                # Template de variáveis de ambiente
├── pom.xml
└── mvnw / mvnw.cmd                             # Maven Wrapper (3.9.14)
```

---

## Pré-requisitos

### Desenvolvimento local

- **JDK 25** ([Download Temurin](https://adoptium.net/))
- **Maven** — opcional; o projeto inclui o wrapper `./mvnw`
- **PostgreSQL** — apenas se executar com o perfil PostgreSQL em vez de H2

### Com Docker (recomendado)

- **Docker** 24+ e **Docker Compose** v2+ ([Instalação](https://docs.docker.com/get-docker/))
- Nenhuma instalação local de Java ou PostgreSQL é necessária

---

## Configuração do Ambiente

O projeto usa variáveis de ambiente para isolar credenciais do código-fonte. Copie o template e preencha com seus valores:

```bash
cp .env.example .env
```

Edite o arquivo `.env` criado. As variáveis disponíveis são:

```properties
# Segredo JWT — gere um valor aleatório seguro:
# openssl rand -hex 32
JWT_SECRET=TROQUE_POR_UMA_STRING_SECRETA_LONGA

# H2 Database (desenvolvimento local)
DB_H2_URL=jdbc:h2:file:./data/teresadb;DB_CLOSE_DELAY=-1
DB_H2_USERNAME=sa
DB_H2_PASSWORD=

# PostgreSQL — conexão da aplicação Spring Boot
# Em Docker: host = "db" (nome do serviço no docker-compose.yml)
# Em local:  host = "localhost"
DB_PG_URL=jdbc:postgresql://db:5432/teresadb
DB_PG_USERNAME=teresa
DB_PG_PASSWORD=TROQUE_POR_SENHA_FORTE

# PostgreSQL — configuração interna do container Docker
# Deve coincidir com os valores DB_PG_* acima
POSTGRES_DB=teresadb
POSTGRES_USER=teresa
POSTGRES_PASSWORD=TROQUE_POR_SENHA_FORTE
```

> O arquivo `.env` está no `.gitignore` e **nunca deve ser commitado**.

---

## Como Executar

### Desenvolvimento local (H2)

No `application.properties`, certifique-se de que o bloco H2 está ativo (descomente as linhas H2 e comente as PostgreSQL). Em seguida:

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### Com Docker Compose (recomendado para produção)

Sobe automaticamente a aplicação Spring Boot **e** o PostgreSQL em containers isolados:

```bash
# 1. Configure as variáveis
cp .env.example .env
# edite .env com JWT_SECRET, DB_PG_PASSWORD e POSTGRES_PASSWORD reais

# 2. Construa a imagem e suba os containers
docker compose up --build

# 3. Para rodar em background
docker compose up --build -d

# 4. Para parar
docker compose down

# Para remover também os volumes (apaga dados do banco e uploads)
docker compose down -v
```

O primeiro `docker compose up --build` compila o projeto dentro do container (Maven + JDK 25 Alpine), sem precisar de Java instalado localmente.

**Portas e volumes:**
- Aplicação disponível em `http://localhost:8080`
- Volume `postgres_data`: persiste o banco de dados entre restarts
- Volume `uploads_data`: persiste os arquivos enviados pelo painel admin

### JAR direto (sem Docker)

```bash
./mvnw clean package -DskipTests
java -jar target/teresa-0.0.1-SNAPSHOT.jar
```

### Modo debug

```bash
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```

---

## Acesso ao Sistema

Após iniciar a aplicação (local ou Docker), os seguintes endereços estarão disponíveis:

| Recurso | URL |
|---|---|
| Página inicial do portal | http://localhost:8080 |
| Painel administrativo | http://localhost:8080/admin/login.html |
| Console H2 (apenas em dev local) | http://localhost:8080/h2-console |

**Credenciais padrão do admin** (criadas automaticamente pelo `DataSeeder`):

| Campo | Valor |
|---|---|
| Usuário | `admin` |
| Senha | `admin123` |

> Altere a senha após o primeiro acesso em produção.

**Conexão ao console H2:**

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/teresadb;DB_CLOSE_DELAY=-1` |
| User Name | `sa` |
| Password | *(vazio)* |

---

## API REST — Referência de Endpoints

Todas as respostas retornam `Content-Type: application/json`.  
Rotas protegidas exigem o cabeçalho `Authorization: Bearer <token>`.

### Autenticação

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/auth/login` | Não | Autentica e retorna o JWT |

**Exemplo de login:**
```json
POST /api/auth/login
{ "username": "admin", "password": "admin123" }

Resposta: { "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

### Projetos

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/projects` | Não | Lista todos os projetos |
| GET | `/api/projects/{id}` | Não | Busca projeto por ID |
| POST | `/api/projects` | Sim | Cria novo projeto |
| PUT | `/api/projects/{id}` | Sim | Atualiza projeto |
| DELETE | `/api/projects/{id}` | Sim | Remove projeto |

### Notícias

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/news` | Não | Lista notícias publicadas |
| GET | `/api/news/all` | Não | Lista todas (incluindo inativas) |
| POST | `/api/news` | Sim | Cria notícia |
| PUT | `/api/news/{id}` | Sim | Atualiza notícia |
| DELETE | `/api/news/{id}` | Sim | Remove notícia |

### Diretoria (Board)

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/board` | Não | Lista membros da diretoria |
| POST | `/api/board` | Sim | Adiciona membro |
| PUT | `/api/board/{id}` | Sim | Atualiza membro |
| DELETE | `/api/board/{id}` | Sim | Remove membro |

### Voluntários

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/volunteers` | Não | Registra candidatura de voluntário |
| GET | `/api/volunteers` | Sim | Lista candidaturas (inclui `motivation`) |
| GET | `/api/volunteer/page` | Não | Conteúdo da página de voluntariado |
| PUT | `/api/volunteer/page` | Sim | Atualiza textos e benefícios da página |

### Discografia

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/discography` | Não | Lista todas as faixas |
| POST | `/api/discography` | Sim | Adiciona faixa |
| PUT | `/api/discography/{id}` | Sim | Atualiza faixa |
| DELETE | `/api/discography/{id}` | Sim | Remove faixa |

### Configurações do Site

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| GET | `/api/site-settings` | Não | Retorna configurações globais |
| PUT | `/api/site-settings` | Sim | Atualiza configurações |

### Upload de Mídias

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/upload` | Sim | Envia imagem (JPG/PNG/GIF/WEBP) — retorna `{ url }` |
| POST | `/api/upload/audio` | Sim | Envia áudio (MP3/OGG/WAV/AAC/FLAC) — retorna `{ url }` |

---

## Modelo de Dados

### Entidades principais

```
Project
├── id, code, title, subtitle, description, impact, image
├── features: List<ProjectFeature>      (@ElementCollection → tabela project_features)
└── details:  Map<String, String>       (@ElementCollection → tabela project_details)

News
└── id, title, summary, content, imageUrl, videoUrl, publishedAt, active

BoardMember
└── id, name, role, imageUrl, icon

Volunteer
└── id, name, email, phone, age, motivation

VolunteerPage
├── id, title1, title2, description
└── benefits: List<VolunteerBenefit>    (@OneToMany)

VolunteerBenefit
└── id, icon, title, description

DiscographyTrack
└── id, code, title, artist, audioFile

SiteSettings  (singleton — id fixo = 1)
└── heroTitle, heroSubtitle, aboutTitle, aboutDescription,
    aboutItem1..2 (icon, title, description),
    ctaTitle, ctaDescription, ctaBullet1..4,
    contactPhone, contactEmail,
    locationCity, locationSubtitle, locationDistance,
    locationCommunity, locationCommunityDetail

AdminUser  (implements UserDetails)
└── id, username, password (BCrypt)
```

---

## Segurança e Autenticação

### Política de Acesso

| Perfil | Rotas permitidas |
|---|---|
| **Público** | `GET /api/**`, `POST /api/auth/login`, `POST /api/volunteers`, arquivos estáticos (`/`, `/*.html`, `/scripts/**`, `/styles/**`, `/assets/**`, `/uploads/**`, `/admin/**`) |
| **Administrador** | `POST/PUT/DELETE /api/projects/**`, `POST/PUT/DELETE /api/board/**`, `POST/PUT/DELETE /api/discography/**`, `POST/PUT/DELETE /api/news/**`, `PUT /api/volunteer/page/**`, `PUT /api/site-settings/**`, `POST /api/upload`, `POST /api/upload/audio` |

### Detalhes técnicos

- Algoritmo de assinatura JWT: **HMAC256**
- Senha do admin armazenada com hash **BCrypt**
- Sessão **Stateless** — nenhum estado é mantido no servidor
- O secret do JWT é injetado via variável de ambiente `JWT_SECRET`
- Filtro `SecurityFilter` implementa `OncePerRequestFilter` — executa uma vez por requisição

---

## Upload de Mídias

Arquivos enviados são:
1. Validados por tipo MIME (rejeita extensões não permitidas)
2. Renomeados com **UUID** gerado aleatoriamente (evita colisões e path traversal)
3. Salvos na pasta `./uploads/` na raiz do projeto (fora do JAR)
4. Servidos publicamente em `/uploads/**`

**Limites:** 10 MB por arquivo (configurável em `application.properties`).

**Em Docker:** os uploads são persistidos no volume nomeado `uploads_data`, montado em `/app/uploads` dentro do container. Os arquivos sobrevivem a restarts e rebuilds da imagem.

**Uso no player de discografia:** O campo `audioFile` aceita:
- Caminho relativo para arquivo local: `./assets/discografia/00 - Poema para Vila Bela` (o player acrescenta `.mp3`)
- URL de upload: `/uploads/uuid.mp3` (retornado após envio via painel admin)

---

## Painel Administrativo (CMS)

O painel em `/admin/` é uma SPA (Single Page Application) em HTML/JS puro que consome a API REST com autenticação JWT armazenada no `localStorage`.

| Página | Função |
|---|---|
| `login.html` | Autenticação JWT |
| `index.html` | Dashboard com links para os módulos |
| `projects.html` | CRUD de projetos com seletor de imagem |
| `news.html` | CRUD de notícias com picker de imagem e vídeo YouTube |
| `board.html` | CRUD de membros da diretoria com seletor de ícone |
| `volunteers.html` | Editor da página pública + lista de candidaturas com modal de motivação |
| `discography.html` | CRUD de faixas + upload de áudio com player de preview |
| `settings.html` | Editor de configurações globais (contato, textos, redes sociais) |

---

## Testes

Os testes são de integração, utilizando **MockMvc** com slice `@WebMvcTest` e as configurações de segurança reais (`@Import(SecurityConfig.class)`).

```bash
# Rodar todos os testes
./mvnw clean test

# Rodar uma classe específica
./mvnw test -Dtest=ProjectControllerTest
./mvnw test -Dtest=DiscographyControllerTest
./mvnw test -Dtest=VolunteerControllerTest
```

**Estrutura dos testes de controller:**

Cada teste usa `@WebMvcTest` com `@Import(SecurityConfig.class)` para carregar as regras de rota reais, e declara `@MockitoBean` para os serviços dependentes e para os beans de infraestrutura de segurança (`TokenService`, `AdminUserRepository`).

Cobertura:
- Validação de inputs inválidos nos endpoints (campos obrigatórios, formatos)
- Persistência e retorno correto de dados
- Respostas HTTP esperadas (200, 201, 400, 404)

---

## Banco de Dados

### Desenvolvimento — H2

Banco de dados relacional em arquivo, sem instalação necessária.

```properties
# No .env:
DB_H2_URL=jdbc:h2:file:./data/teresadb;DB_CLOSE_DELAY=-1
DB_H2_USERNAME=sa
DB_H2_PASSWORD=
```

No `application.properties`, descomente o bloco H2 e comente o bloco PostgreSQL.

O `DataSeeder` popula automaticamente o banco no primeiro início com:
- Usuário admin padrão (`admin` / `admin123`)
- 3 projetos institucionais (Festança do Congo, Festival de Praia, Dia da Consciência Negra)
- 16 faixas da discografia (Quilombo Aurora do Quariterê)
- 3 membros da diretoria
- Configuração inicial da página de voluntários
- Configurações globais do site
- 1 notícia de exemplo

### Produção — PostgreSQL 17

Em ambiente Docker, o container `db` (postgres:17-alpine) é provisionado automaticamente pelo `docker-compose.yml`. Em ambiente bare-metal, crie o banco manualmente:

```sql
CREATE DATABASE teresadb;
```

A estrutura das tabelas é criada automaticamente pelo Hibernate (`ddl-auto=update`).

---

## Migração para Produção

### Opção A — Docker Compose (recomendada)

```bash
# Configure as variáveis de ambiente
cp .env.example .env
# Edite .env: JWT_SECRET, DB_PG_PASSWORD, POSTGRES_PASSWORD

# Suba app + banco em background
docker compose up --build -d

# Verifique os logs
docker compose logs -f app
```

O Docker Compose cuida de:
- Construir a imagem da aplicação (Maven → JAR → imagem JRE 25 Alpine)
- Provisionar o PostgreSQL com healthcheck antes de iniciar o app
- Persistir banco e uploads em volumes nomeados
- Reiniciar os containers automaticamente (`restart: unless-stopped`)

### Opção B — JAR direto no servidor

#### 1. Configurar o banco PostgreSQL

```sql
CREATE DATABASE teresadb;
```

#### 2. Ativar o perfil PostgreSQL

Em `src/main/resources/application.properties`:
- **Comente** todo o bloco `H2 Database (desenvolvimento local)`
- **Descomente** todo o bloco `PostgreSQL (produção / staging)`

#### 3. Definir as variáveis de ambiente no servidor

```bash
export DB_PG_URL=jdbc:postgresql://localhost:5432/teresadb
export DB_PG_USERNAME=seu_usuario
export DB_PG_PASSWORD=sua_senha
export JWT_SECRET=$(openssl rand -hex 32)
```

#### 4. Gerar e executar o JAR

```bash
./mvnw clean package -DskipTests
java -jar target/teresa-0.0.1-SNAPSHOT.jar
```

---

### Opção C — Render (PaaS)

Deploy gerenciado no [Render](https://render.com) usando o `Dockerfile.render`, que adapta a porta via `$PORT` e adiciona flags de JVM para containers.

#### 1. Criar o Web Service no Render

No dashboard do Render, crie um **Web Service** e conecte o repositório GitHub. Configure:

| Campo | Valor |
|---|---|
| Environment | Docker |
| Dockerfile Path | `./Dockerfile.render` |
| Docker Build Context | `.` |

#### 2. Configurar as variáveis de ambiente

Na aba **Environment** do Web Service, adicione as seguintes variáveis:

| Variável | Valor |
|---|---|
| `JWT_SECRET` | string aleatória longa — gere com `openssl rand -base64 48` |
| `DB_PG_URL` | URL JDBC do banco no Render (veja abaixo) |
| `DB_PG_USERNAME` | usuário do banco PostgreSQL no Render |
| `DB_PG_PASSWORD` | senha do banco PostgreSQL no Render |

#### 3. Obter a URL JDBC do banco PostgreSQL

No dashboard do banco PostgreSQL criado no Render, acesse **Connections → Internal Database URL**. O Render exibe no formato `postgres://...`; converta para o formato JDBC trocando apenas o prefixo:

```
# O Render exibe:
postgres://usuario:senha@host/banco

# Use em DB_PG_URL:
jdbc:postgresql://host/banco
```

> Use a **Internal URL** (não a External) para comunicação interna entre os serviços no Render — sem latência de rede pública e sem custo de egress.

#### 4. Uploads persistentes (opcional)

Os arquivos enviados pelo painel admin são salvos em `/app/uploads` dentro do container. Sem persistência configurada, eles são perdidos a cada redeploy.

Para manter os uploads entre deploys, adicione um **Disk** no painel do Web Service:

| Campo | Valor |
|---|---|
| Mount Path | `/app/uploads` |
| Size | conforme necessidade (mínimo 1 GB) |

---

*Feito com dedicação à preservação da cultura quilombola e à memória de Tereza de Benguela.*
