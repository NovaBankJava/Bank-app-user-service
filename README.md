# NovaBank — User Service (`Bank-app-user-service`)

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-6DB33F)
![Build](https://img.shields.io/badge/build-Maven-blue)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

Serviço de **usuários** do NovaBank — um app bancário construído de forma
colaborativa e open source, em Java. Este microsserviço cuida de tudo em torno da
identidade e da conta do cliente: cadastro, autenticação, perfil, contas
bancárias vinculadas, notificações e relatórios.

> Projeto de estudo e colaboração. A ideia é aprender construindo algo real, com
> boas práticas de engenharia desde o começo.
>
> **Idioma:** o código é em inglês (classes, métodos, pacotes, comentários). A
> documentação explicativa, como este README, fica em português.

---

## Sumário

- [Stack](#stack)
- [Como rodar](#como-rodar)
- [Arquitetura hexagonal](#arquitetura-hexagonal)
- [Estrutura de pastas](#estrutura-de-pastas-por-feature)
- [Feature de referência: account](#feature-de-referência-account)
- [Testes](#testes)
- [Qualidade de código](#qualidade-de-código)
- [Fluxo de contribuição](#fluxo-de-contribuição)
- [Roadmap](#roadmap)

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | **Java 21** |
| Framework | **Spring Boot 4.0.7** |
| Web | Spring Web MVC + Spring WebFlux |
| Persistência | Spring Data JPA |
| Banco (dev) | H2 (em memória) |
| Sessão | Spring Session + Redis |
| Boilerplate | Lombok (apenas na borda de persistência) |
| Build | Maven (via wrapper `./mvnw`) |

---

## Como rodar

**Pré-requisitos:** JDK 21 e Redis (necessário para a sessão).

Subir o Redis via Docker:
```bash
docker run -d --name novabank-redis -p 6379:6379 redis:7
```

Rodar a aplicação:
```bash
./mvnw spring-boot:run       # Linux/macOS
mvnw.cmd spring-boot:run     # Windows
```

Rodar os testes:
```bash
./mvnw test
```

> Os testes de integração (`*IT`) sobem o contexto completo e precisam do Redis
> no ar. Os testes de domínio e de use case rodam sem Redis nem banco.

---

## Arquitetura hexagonal

O projeto segue **Arquitetura Hexagonal** (Ports & Adapters). A regra central:
**dependências apontam pra dentro**. O domínio (regras de negócio) fica no centro
e não conhece Spring, JPA, HTTP nem banco. Tudo que é técnico vive na borda e se
conecta ao centro através de interfaces.

```
   HTTP / REST                                   Banco / Redis
        |                                               |
  [inbound adapter] -> [inbound port] -> DOMÍNIO -> [outbound port] -> [outbound adapter]
   Controller           UseCase          Regras     RepositoryPort      impl JPA
```

- **Porta de entrada (`port/in`)**: interface do caso de uso (ex.: `CreateAccountUseCase`).
  O controller depende dela, nunca do service concreto.
- **Porta de saída (`port/out`)**: interface do que o domínio precisa do mundo
  externo (ex.: `AccountRepositoryPort`). O domínio a define; o adapter a implementa.
- **Domínio**: entidades e regras em Java puro, sem anotação de framework.

Como o domínio é dono da porta de saída e o adapter a implementa, trocar o banco
(H2 -> Postgres) mexe só no adapter. O centro não fica sabendo.

### Duas entidades, de propósito

A entidade de domínio (ex.: `Account`) e a entidade JPA (`AccountJpaEntity`) são
**classes diferentes**, ligadas por um mapper. O domínio é Java puro; as
anotações de JPA e o Lombok ficam só na entidade de persistência. É o que mantém
o núcleo limpo e trivial de testar.

Guia completo em `HEXAGONAL-ARCHITECTURE.md`.

---

## Estrutura de pastas (por feature)

Cada feature (`account`, `user`, ...) segue o mesmo formato:

```
<feature>/
├── domain/
│   ├── model/          # entidades de domínio + enums (Java puro)
│   └── exception/      # exceções de negócio
├── application/
│   ├── port/
│   │   ├── in/         # portas de entrada (interfaces de caso de uso)
│   │   └── out/        # portas de saída (interfaces de repositório)
│   └── service/        # implementações dos casos de uso
└── adapter/
    ├── in/
    │   └── web/        # controllers + DTOs
    └── out/
        └── persistence/# entidade JPA, repositório, mapper, adapter
```

---

## Feature de referência: account

A feature `account` (issue #34, cadastro e gestão de conta bancária vinculada) é
a **referência** da estrutura hexagonal para o time — copie o padrão dela ao
começar uma feature nova.

Endpoints (`/api/v1/users/{userId}/accounts`):

| Método | Rota | Ação |
|---|---|---|
| POST   | `/` | Cria conta (a primeira do usuário vira principal automaticamente) |
| GET    | `/` | Lista as contas do usuário |
| PATCH  | `/{accountId}/primary` | Define a conta como principal (desmarca as outras) |
| DELETE | `/{accountId}` | Remove a conta |

Regras de negócio no domínio: um usuário tem no máximo uma conta principal por
vez; a primeira conta vira principal automaticamente; uma conta só pode ser
manipulada pelo usuário dono dela.

---

## Testes

A feature segue uma pirâmide de testes, cada nível no seu lugar:

- **Domínio** — testes unitários puros (`AccountTest`), sem Spring nem banco.
  Rodam em milissegundos.
- **Aplicação** — teste do service (`AccountServiceTest`) com um fake da porta de
  saída (lista em memória). Também sem Spring nem banco.
- **Persistência** — `@DataJpaTest` (`AccountPersistenceAdapterTest`) sobe só a
  fatia de JPA + H2 para validar o adapter contra um banco real.
- **Web** — `@SpringBootTest` (`AccountControllerIT`) sobe a app inteira e bate
  nos endpoints via MockMvc.

O ganho da hexagonal aparece aqui: as regras de negócio são testadas sem
infraestrutura, porque o domínio não depende dela.

---

## Qualidade de código

Proposta de padronização (rodam no build):

- **Spotless** com `google-java-format` — formatação automática (`./mvnw spotless:apply`).
- **Checkstyle** — regras de nomenclatura, imports, chaves obrigatórias.

Arquivos de apoio: `checkstyle.xml`, `.editorconfig`, e um CI opcional em
`.github/workflows/ci.yml` que roda `./mvnw verify` em cada PR.

---

## Fluxo de contribuição

1. Escolha uma issue e se atribua a ela no GitHub.
2. Crie uma branch a partir da `main`: `git checkout -b feat/<n>-descricao`.
3. Rode `./mvnw test` antes de commitar.
4. Commits em inglês, no padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/)
   (`feat:`, `fix:`, `docs:`...).
5. Abra o PR referenciando a issue (`Closes #<n>`) e peça review.

---

## Roadmap

São 36 tasks nas [issues do repositório](https://github.com/NovaBankJava/Bank-app-user-service/issues),
desenvolvidas em fases: **primeiro tudo do usuário, depois a parte de segurança.**

**Fase 1 — Usuário:** cadastro de usuário, perfil, exclusão de conta (LGPD),
conta bancária vinculada (#34, concluída).

**Fase 2 — Segurança:** login, recuperação de senha, 2FA, biometria, KYC.

**Fase posterior:** notificações e relatórios.

Projeto liderado pelo **Valmir** ([@ValmirboJr](https://github.com/ValmirboJr)).
