# NovaBank — User Service (`Bank-app-user-service`)

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-6DB33F)
![Build](https://img.shields.io/badge/build-Maven-blue)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

Serviço de **usuários** do NovaBank — um app bancário construído de forma
colaborativa e open source, em Java. Este microsserviço cuida da identidade e da
conta do cliente: cadastro, autenticação, perfil, contas bancárias vinculadas,
notificações e relatórios.

> Projeto de estudo e colaboração, com boas práticas de engenharia desde o começo.
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
- [Convenções do time](#convenções-do-time)
- [Testes](#testes)
- [Fluxo de contribuição](#fluxo-de-contribuição)

---

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | **Java 21** |
| Framework | **Spring Boot 4.0.7** |
| Web | Spring Web MVC + Spring WebFlux |
| Persistência | Spring Data JPA |
| Validação | Spring Validation (Hibernate Validator) |
| Banco (dev) | H2 (em memória) |
| Sessão | Spring Session + Redis |
| Boilerplate | Lombok |
| Build | Maven (via wrapper `./mvnw`) |

---

## Como rodar

**Pré-requisitos:** JDK 21 e Redis (necessário para a sessão).

```bash
docker run -d --name novabank-redis -p 6379:6379 redis:7   # Redis
./mvnw spring-boot:run                                      # app
./mvnw test                                                 # testes
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

- **Porta de entrada (`port/in`)**: interface do caso de uso. O controller depende
  dela, nunca do service concreto.
- **Porta de saída (`port/out`)**: interface do que o domínio precisa do mundo
  externo. O domínio a define; o adapter a implementa.
- **Domínio**: entidades e regras em Java puro, sem anotação de framework.

Trocar o banco (H2 -> Postgres) mexe só no adapter — o centro não fica sabendo.
A entidade de domínio (`Account`) e a entidade JPA (`AccountEntity`) são
**classes diferentes**, ligadas por um mapper: o domínio é puro, as anotações de
JPA/Lombok ficam só na borda de persistência.

Guia completo em `HEXAGONAL-ARCHITECTURE.md`.

---

## Estrutura de pastas (por feature)

```
<feature>/
├── domain/
│   ├── model/          # entidades de domínio + enums (Java puro)
│   ├── exception/      # exceções de negócio
│   └── validation/     # validadores puros (ex.: CpfValidator)
├── application/
│   ├── port/
│   │   ├── in/         # portas de entrada (interfaces de caso de uso)
│   │   └── out/        # portas de saída (repositório, consulta a outras features)
│   └── service/        # implementações dos casos de uso
└── adapter/
    ├── in/
    │   └── web/        # controllers + DTOs
    └── out/
        ├── persistence/# entidade JPA, repositório, mapper, adapter
        └── user/       # consulta ao usuário (stub até a feature de usuário existir)
```

---

## Feature de referência: account

A feature `account` (issue #34) é a **referência** da estrutura hexagonal — copie
o padrão dela ao começar uma feature nova.

Endpoints (`/api/v1/accounts`):

| Método | Rota | Ação |
|---|---|---|
| POST   | `/createAccount` | Cria conta |
| GET    | `/listAccounts/{userId}` | Lista as contas do usuário |
| PATCH  | `/setPrimaryAccount/{userId}/{accountId}` | Define a conta como principal |
| DELETE | `/deleteAccount/{userId}/{accountId}` | Remove a conta |

### Regras de negócio
- A primeira conta cadastrada de um usuário vira principal automaticamente.
- Um usuário tem no máximo uma conta principal por vez.
- Uma conta só pode ser manipulada pelo usuário dono dela.
- O CPF informado no cadastro deve ser o mesmo CPF do usuário (titularidade).
- Usuário menor de idade não pode ter conta do tipo salário.
- Não é possível cadastrar a mesma conta (banco + agência + número) duas vezes.
- Guarda a data de criação do cadastro (`createdAt`).

### Consulta ao usuário
As regras de titularidade e idade dependem de dados do usuário (CPF, data de
nascimento). A feature acessa isso por uma porta de saída (`UserLookupPort`).
Enquanto a feature de usuário não existe, há um **stub** (`StubUserLookupAdapter`)
que deve ser substituído pela implementação real quando o usuário estiver pronto.

---

## Convenções do time

- **Respostas sempre HTTP 200** com um envelope padrão `{ status, description, data }`.
  Erros de negócio viram um `status` no corpo, não um código HTTP de erro.
  Tabela de status centralizada em `ApiResponse`.
- **Endpoints com rota nomeada** (`/createAccount`), não apenas `/`.
- **`@Data`** na entidade JPA.
- **Logs em abundância** (`@Slf4j`, `log.info` nos casos de uso).
- **Validação em duas camadas**: formato/obrigatórios via `@Valid` no DTO
  (`@NotBlank`, `@CPF`) e também no service (defesa em profundidade). Regras de
  negócio ficam só no service.

---

## Testes

Pirâmide de testes, cada nível no seu lugar:

- **Domínio** — unitário puro (`AccountTest`), sem Spring nem banco.
- **Aplicação** — service com fakes das portas (`AccountServiceTest`), sem Spring
  nem banco. Cobre todas as regras e validações.
- **Persistência** — `@DataJpaTest` com H2 (`AccountAdapterTest`).
- **Web** — `@SpringBootTest` + MockMvc (`AccountControllerIT`).

---

## Fluxo de contribuição

1. Escolha uma issue e se atribua a ela no GitHub.
2. Clone o repositório do time (NovaBankJava) e crie uma branch: `git checkout -b feat/<n>-descricao`.
3. Rode `./mvnw test` antes de commitar.
4. Commits em inglês, no padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/).
5. Abra o PR referenciando a issue (`Closes #<n>`) e peça review.

Projeto liderado pelo **Valmir** ([@ValmirboJr](https://github.com/ValmirboJr)).
