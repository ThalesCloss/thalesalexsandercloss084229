# Desafio Técnico - Engenheiro da Computação: Desenvolvedor Back End

**Candidato:** Thales Alexsander Closs  
**Inscrição:** 16406

Este repositório contém a solução para o desafio técnico proposto. A aplicação foi desenvolvida com foco em arquitetura limpa, escalabilidade e prontidão para ambientes de nuvem (Cloud-Ready), utilizando Java 21 e Quarkus.

## 🚀 Como Executar

A imagem da aplicação já está publicada no Docker Hub, eliminando a necessidade de compilação local (build) para a execução padrão.

### Execução Rápida (Imagem Remota)

```bash
docker-compose up -d
```

### Portas e Serviços
Após a inicialização, os serviços estarão disponíveis nos seguintes endereços:

| Serviço | Porta/URL | Descrição |
| :--- | :--- | :--- |
| **Aplicação (Via Nginx)** | `http://localhost:8889` | Ponto de entrada (Load Balancer) |
| **Documentação API** | `http://localhost:8889/q/swagger-ui` | Swagger UI (Interação com a API) |
| **Keycloak** | `http://localhost:9497` | Identity Provider |
| **MinIO** | `http://localhost:9000` | Object Storage (S3 Compatible) |
| **Jaeger UI** | `http://localhost:16686` | Visualização de Tracing (OpenTelemetry) |

### Execução em Modo de Desenvolvimento (Build Local)
Caso deseje alterar o código e rodar com o compilador local:

```bash
docker-compose -f docker-compose.development.yml up --build
```

```bash
./mvnw compile quarkus:dev
```

---
## 🔑 Dados de Teste e Configuração de Acesso

O ambiente sobe pré-configurado com usuários distintos para **consumo da API** (simulando clientes com diferentes níveis de permissão) e para **administração da infraestrutura**.

### 1. Autenticação na API (Swagger UI / Postman)

A API utiliza o padrão **OpenID Connect** com o fluxo mais seguro para aplicações públicas.

* **Fluxo:** Authorization Code com PKCE
* **Client ID:** `seletivo-seplag-client`
* **Discovery / Realm URL:** `http://localhost:9497/realms/seletivo-seplag-realm`
* **Scopes:** `openid`, `profile`, `email` 

#### ⚠️ Importante: Expiração de Token e Renovação
Por padrão de segurança, o **Access Token tem validade de apenas 5 minutos**.
* **Comportamento no Swagger:** O Swagger UI **não renova** o token automaticamente. Quando o token expirar (erro 401), você deve clicar no botão **Logout** (cadeado aberto) e depois em **Authorize** novamente.
* **Sessão SSO:** Caso esteja dentro da janela de sessão (padrão de 30 min), a renovação será transparente, sem exigir a digitação de login e senha novamente.

**Dica para Testadores:**
Caso deseje aumentar esses tempos para evitar interrupções durante os testes manuais, acesse o painel administrativo do Keycloak (credenciais abaixo) e ajuste:
1.  **Aumentar Duração do Token:** [Realm Settings > Tokens > Access Token Lifespan](http://localhost:9497/admin/master/console/#/seletivo-seplag-realm/realm-settings/tokens)
2.  **Aumentar Duração da Sessão:** [Realm Settings > Sessions > SSO Session Idle](http://localhost:9497/admin/master/console/#/seletivo-seplag-realm/realm-settings/sessions)

#### Usuários Disponíveis
Utilize estas credenciais para testar as regras de negócio e controle de acesso (RBAC).

| Usuário | Senha | Contexto / Permissões |
| :--- | :--- | :--- |
| `teste` | `teste` | **Acesso Total.** Simula um gestor que pode cadastrar álbuns, artistas e executar a **Sincronização de Regionais**. |
| `teste-sem-regional` | `teste` | **Acesso Restrito.** Simula um operador comum. Tem acesso aos cadastros gerais, mas **NÃO** tem permissão para acionar a Sincronização de Regionais (retorna 403 Forbidden). | 

#### 👤 Gestão de Conta e Novos Usuários
O ambiente suporta o cadastro de novos usuários (Self-Service) e gestão de sessão.

* **Criar Novo Usuário:** Via link "Register" na tela de login.
* **Logout / Minha Conta:** [http://localhost:9497/realms/seletivo-seplag-realm/account](http://localhost:9497/realms/seletivo-seplag-realm/account)

**Nota sobre Permissões de Novos Usuários:**
Por padrão de segurança, novos usuários criados via auto-cadastro **NÃO possuem permissão** para sincronizar regionais (comportamento igual ao usuário `teste-sem-regional`).
Caso deseje conceder essa permissão a um novo usuário para testes:
1.  Acesse o Keycloak Admin.
2.  Vá em *Users* > *Selecione o Usuário* > *Role Mapping*.
3.  Atribua a Role necessária (exemplo de configuração no link abaixo):
    * [Link direto para Role Mapping (Exemplo)](http://localhost:9497/admin/master/console/#/seletivo-seplag-realm/users/5727b2ab-5dfb-4439-a67f-613ae5002388/role-mapping)

### 2. Acesso à Infraestrutura (Consoles Administrativos)
Utilize estas credenciais para acessar os painéis de gerenciamento dos serviços de suporte.

| Serviço | URL | Usuário | Senha | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| **Keycloak Admin** | `http://localhost:9497` | `admin` | `admin` | Gestão de Realms, Clientes e Users (Identity Provider). |
| **MinIO Console** | `http://localhost:9000` | `admin` | `password` | Gestão de Buckets e Arquivos (Object Storage). |
| **Jaeger UI** | `http://localhost:16686` | *(N/A)* | *(N/A)* | Visualização de Traces e Spans. |
---

## 🗄️ Versionamento de Banco de Dados (Flyway)

O gerenciamento do ciclo de vida do banco de dados é totalmente automatizado via **Flyway**. Ao iniciar a aplicação (via Docker Compose ou localmente), o sistema verifica e aplica as migrações pendentes.

### O que é executado?
1.  **Estrutura (DDL):** Criação automática de todas as tabelas (`albums`, `artists`, `regionais`, etc), índices e chaves estrangeiras, garantindo que o Schema esteja sempre sincronizado com a versão do código.
2.  **Seed de Dados (DML):** Uma migração específica (`V1.1.1__Seed.sql`) popula o banco com dados iniciais essenciais para teste, incluindo:
    * Usuários padrão para teste de autenticação.
    * Dados de domínio básicos (se houver).
    * *Isso permite que a API seja testada imediatamente após o `docker-compose up`, sem necessidade de configuração manual.*

### 🛡️ Concorrência e Segurança
Para ambientes containerizados com réplicas, o Flyway utiliza um mecanismo de **Database Locking** na tabela de controle `flyway_schema_history`.
* Isso garante que, mesmo que múltiplas instâncias da API subam simultaneamente, a migração seja executada por apenas uma delas, enquanto as outras aguardam o término do processo de forma segura.

---

## 🏗️ Arquitetura da Solução

### Diagrama de Container (C4 L2)

O diagrama abaixo ilustra os componentes da solução conteinerizada.

```mermaid
C4Context
    title Diagrama de Container - Arquitetura da Solução

    Person(user, "Usuário / QA", "Utiliza o Swagger UI para interagir e testar os endpoints da API.")
    Person(sre, "SRE / DevOps", "Monitora a saúde, performance e traces da aplicação.")

    System_Ext(gea, "Integrador GEA", "Sistema Externo de Regionais")

    Boundary(b1, "Ambiente Docker Compose", "Rede Interna") {
        
        Container(nginx, "Nginx", "Load Balancer - :8889", "Distribui tráfego com cabeçalho de identificação da réplica.")
        
        Container(api, "API Backend", "Java 21 / Quarkus", "(2 Réplicas)")
        
        Container(kc, "Keycloak", "Identity Provider - :9497", "Gestão de Identidade, fluxo Auth Code + PKCE.")
        
        ContainerDb(redis, "Redis", "Cache / PubSub", "Backend para Rate Limit Distribuído e mensageria WebSocket.")
        
        ContainerDb(db, "PostgreSQL", "RDBMS", "Persistência dos dados.")
        
        Container(minio, "MinIO", "S3 Compatible - :9000", "Armazenamento de arquivos (Bucket Privado).")
        
        Container(jaeger, "Jaeger All-in-One", "OpenTelemetry - :16686", "Coleta e visualização de traces distribuídos.")
    }

    %% Fluxos do Usuário
    Rel(user, nginx, "Requisições HTTP", "Via Swagger UI")
    Rel(user, kc, "Autenticação", "Browser Redirect")
    Rel(user, minio, "Upload Direto (PUT)", "Link Pré-assinado")

    %% Fluxos do SRE
    Rel(sre, jaeger, "Analisa Traces", "Jaeger UI")
    Rel(sre, minio, "Gestão de Buckets", "MinIO Console")
    Rel(sre, kc, "Gestão de Usuários", "Admin Console")

    %% Fluxos Internos da Aplicação
    Rel(nginx, api, "Encaminha (Round Robin)", "HTTP")
    Rel(api, kc, "Valida Token (Introspection/JWT)", "HTTP")
    Rel(api, db, "Leitura/Escrita", "JDBC/jOOQ")
    Rel(api, redis, "Check Rate Limit / PubSub", "RESP")
    Rel(api, minio, "Gera Link Assinado (Dual Client)", "S3 API")
    Rel(api, gea, "Sincroniza Regionais", "HTTP")
    Rel(api, jaeger, "Envia Telemetria", "gRPC")

    UpdateLayoutConfig($c4ShapeInRow="3", $c4BoundaryInRow="1")
```
---

## 🧠 Decisões Arquiteturais e Design

A solução apresentada excede intencionalmente a complexidade necessária para os requisitos básicos do desafio. O objetivo foi utilizar este projeto como um laboratório para demonstrar minha abordagem na **resolução de problemas reais**, **modelagem de domínios complexos** e **conhecimento de padrões de mercado**.

Principais escolhas e motivações:

* **Expansão do Domínio (DDD):** Ao invés de manter apenas o relacionamento simples entre Álbum e Artista, optei por enriquecer o domínio com conceitos reais da indústria (como *Lineups*, *Composições* e *Membros*). O objetivo é demonstrar capacidade de traduzir regras de negócio complexas e invariantes em código expressivo.
* **Arquitetura Hexagonal:** A separação estrita entre Domínio e Infraestrutura foi adotada para facilitar a testabilidade e proteger as regras de negócio. Reconheço que adiciona indireção (Ports/Adapters), mas demonstra preocupação com a longevidade e manutenção do software.
* **Redis como Solução para Sistemas Distribuídos:** O Redis foi utilizado além do cache tradicional, atuando como peça chave para resolver problemas de estado em uma arquitetura escalável:
    * *Rate Limit Distribuído:* Implementação de algoritmo de janela fixa compartilhada, garantindo que o limite de requisições seja respeitado globalmente, independente do número de réplicas da API.  
    * **Feedback ao Cliente:** A API segue boas práticas de transparência, retornando em cada resposta headers informativos (`X-Rate-Limit-Remaining`, `X-Rate-Limit-Limit`), permitindo que o consumidor monitore seu saldo de requisições em tempo real.
    * *Notificações em Cluster (Pub/Sub):* Solução para o problema de conexões WebSocket em ambiente balanceado. Eventos são publicados no Redis para garantir que usuários recebam notificações independentemente da instância em que estão conectados.
* **System Design e Resiliência:**
    * *Load Balancing:* Simulação de ambiente produtivo com Nginx distribuindo carga entre duas réplicas da aplicação, injetando headers para rastreabilidade da instância processadora `x-served-by`.
    * *Health Checks:* Implementação de verificações profundas (Deep Health Check). A aplicação monitora ativamente suas dependências (Banco, Broker, IDP) e informa ao orquestrador (Docker/K8s) se está apta a receber tráfego.
    * *Observabilidade:* Instrumentação completa com **OpenTelemetry**, permitindo o rastreamento distribuído (Tracing) visualizável no **Jaeger** para análise de gargalos e falhas.
* **Estratégia de Testes:** A qualidade foi assegurada através de uma pirâmide de testes diversificada:
    * *Unitários:* Focados estritamente nas regras e invariantes do Domínio.
    * *Componente:* Validação isolada de integrações complexas (ex: Sincronização de Regionais).
    * *E2E:* Testes de ponta a ponta cobrindo fluxos críticos, incluindo a validação de entrega de mensagens assíncronas via WebSocket.
* **Segurança (Keycloak & OAuth2):** Integração com Identity Provider utilizando fluxo **Authorization Code + PKCE**, demonstrando familiaridade com os padrões de segurança exigidos em aplicações modernas (SPA).
* **CQRS e Separação de Leitura/Escrita:** Convivência entre **jOOQ** (Leitura performática) e **Hibernate** (Comandos/Consistência) na mesma base, preparando o terreno para uma eventual segregação física de dados.

---

## 🎵 O Domínio Musical (Regras de Negócio)

O coração desta aplicação é um domínio rico que resolve problemas reais de modelagem de dados da indústria fonográfica. Abaixo, são detalhadas as principais entidades e suas invariantes.

### Componentes do Domínio

* **Person (Pessoa):** Representa um indivíduo do mundo real (CPF/Identidade). É a base para compositores e membros de bandas.
* **Composition (Obra):** A propriedade intelectual. A letra/partitura escrita por uma ou mais *Persons*. Uma música pode ser regravada mil vezes (vários Álbuns), mas a *Composition* é a mesma.
* **Artist Profile (Artista):** A "persona" pública. Pode ser um cantor solo, dupla ou banda.
* **Lineup (Formação):** O conceito temporal de um artista. Um Artista possui um histórico de Formações.
    * *Regras de Invariância:*
        * **SOLO:** Deve ter exatamente 1 membro e somente uma formação.
        * **DUO:** Deve ter exatamente 2 membros.
        * **BAND:** Deve ter mais de 2 membros.
    * *Metadados:* Membros podem ser marcados como "Fundador" ou "Artista Principal".
* **Album:** O produto final.
    * *Rastreabilidade:* O álbum é vinculado a uma **Lineup específica**. Isso permite saber exatamente quem tocou naquele disco (ex: fase antiga vs fase nova de uma banda).
* **Track (Faixa):** A materialização de uma *Composition* dentro de um *Album*.
    * Possui metadados de mídia (duração, número do disco, posição, ISRC).
    * **Feat/Convidados:** Um convidado é uma *Person* externa à formação, com um "Nome de Crédito" específico e ordem de relevância.

---
### 🔄 Sincronização de Regionais

A funcionalidade de sincronização com o sistema externo (GEA) exigiu uma abordagem específica de modelagem para garantir integridade e performance.

* **Modelagem (Object Calisthenics):** A manipulação da lista de regionais é feita através de uma *First Class Collection* (Classe Container), tratando a coleção de entidades como um cidadão de primeira classe no domínio.
* **Riqueza do Domínio:** Mesmo sendo uma entidade conceitualmente simples, foi evitado anti-padrão de *Modelo Anêmico*. A classe possui métodos expressivos que encapsulam regras de negócio, como a verificação explícita de alteração de nome e o comando de desativação, tornando o fluxo de sincronização legível e seguro.
* **Estratégia de Persistência (JPA):**
    * *Decisão:* Foi adicionado um **ID interno auto-gerado** à entidade, divergindo da utilização apenas do código externo como chave primária.
    * *Motivação:* A regra de negócio permite a existência de múltiplos registros com o mesmo código de regional (histórico de nomes/versões inativas vs versão ativa). Para evitar chaves compostas complexas e, principalmente, para habilitar o **Dirty Checking** automático do Hibernate, o ID interno foi necessário.
* **Transacionalidade:** O processo é executado em um contexto transacional atômico. O ORM rastreia automaticamente as alterações de estado (desativação de regionais antigas) e as novas inserções. Se qualquer erro ocorrer durante o processamento da lista, o *Rollback* é acionado, garantindo que o banco nunca fique em estado inconsistente.

---


## ✍️ Exemplos de Uso: Fluxo de Criação (Escrita)

Para garantir a consistência dos dados e seguir as regras de negócio (DDD), a API impõe uma ordem lógica de dependências: **Pessoas > Artistas > Composições > Álbuns**.

Não é possível criar um álbum sem antes definir quem o gravou (Lineup) e o que foi gravado (Compositions). Abaixo, a simulação do cadastro de um lançamento do artista **"Djavan"**.

### Passo 1: Criar a Pessoa (Person)
A base do domínio. Antes de existir o "artista" (persona pública), precisamos cadastrar o indivíduo real.

`POST http://localhost:8889/v1/persons`

```json
{
  "name": "Djavan Caetano Viana",
  "birthDate": "1949-01-27"
}
```
> **Retorno:** O sistema gera o ID `019c303f-ef62-7dda-a950-0713fe40306c` que será usado nos próximos passos.

---

### Passo 2: Criar o Artista (Artist Profile)
Agora criamos a entidade pública.
* **Regra de Negócio:** Todo artista nasce com uma **Formação Inicial (Lineup)** obrigatória.
* **Validação:** Como o tipo é `SOLO`, a lista deve conter exatamente 1 membro vinculado a uma `Person` existente.

`POST http://localhost:8889/v1/artist-profile`

```json
{
  "stageName": "Djavan",
  "biography": "Um dos maiores ícones da MPB, conhecido por suas harmonias complexas.",
  "artistType": "SOLO",
  "lineups": [
    {
      "label": "Carreira solo",
      "startDate": "1976-01-01",
      "members": [
        {
          "personId": "019c303f-ef62-7dda-a950-0713fe40306c",
          "isMainArtist": true,
          "isFounder": true
        }
      ]
    }
  ]
}
```
> **Retorno:** O sistema cria o Artista (`019c3043-29df-7ead-bdb8-f38263417ca8`) e a Lineup ativa (`019c3043-29dd-7866-88bd-796b2f39dd19`).

---

### Passo 3: Criar a Obra (Composition)
A propriedade intelectual. Uma composição existe independentemente de ter sido gravada em um álbum e pode ter múltiplos autores (`Persons`).

* **Destaque de Domínio (ISWC):** O payload inclui o campo `iswc` (*International Standard Musical Work Code*).
    * O ISWC é o identificador global único para obras musicais (semelhante ao ISBN para livros).

`POST http://localhost:8889/v1/compositions`

```json
{
  "title": "Samurai",
  "iswc": "T-039.123.987-5",
  "authorIds": [
    "019c303f-ef62-7dda-a950-0713fe40306c"
  ],
  "lyrics": "Ai, quanto querer, cabe em meu coração...",
  "releaseDate": "1982-01-01"
}
```
> **Retorno:** Gera o ID da obra `019c3045-56bc-7723-ad75-a1f5ba51bd44`.

---

### Passo 4: Criar o Álbum
A materialização do produto. O álbum conecta todas as pontas anteriores.
* **Rastreabilidade:** O campo `lineupId` é obrigatório. Isso garante que saibamos exatamente **qual formação** daquele artista gravou este disco específico.
* **Conteúdo:** É obrigatório enviar pelo menos uma faixa (`track`) vinculada a uma `composition`.
* **`isrc` (International Standard Recording Code):** É o identificador único da **gravação sonora** específica. Diferente do ISWC (que identifica a "ideia" da música), o ISRC identifica o arquivo de áudio final. Se uma música for remasterizada ou gravada ao vivo, ela receberá um novo ISRC.
    * **Campo Opcional:** Este identificador **não é obrigatório** para o cadastro.  
* **O campo `contextDuration`:** define a duração temporal da faixa no álbum e deve seguir estritamente o padrão **ISO-8601**, que é o formato nativo suportado pela classe `java.time.Duration`.

`POST http://localhost:8889/v1/albums`

```json
{
  "title": "Luz",
  "artistProfileId": "019c3043-29df-7ead-bdb8-f38263417ca8",
  "artistProfileLineupId": "019c3043-29dd-7866-88bd-796b2f39dd19",
  "releaseDate": "1982-08-01",
  "tracks": [
    {
      "title": "Samurai",
      "compositionId": "019c3045-56bc-7723-ad75-a1f5ba51bd44",
      "discNumber": 1,
      "trackNumber": 3,
      "contexDuration": "PT4M48S",
      "isrc": "BR-SME-82-00003"
    }
  ]
}
```


## 🔎 Exemplos de Uso: Busca Flexível de Álbuns

A API de busca de álbuns foi projetada para explorar a riqueza do domínio musical. Ela permite encontrar obras não apenas pelo nome do artista principal, mas também navegando pelas conexões entre músicos, bandas e participações especiais.

Um exemplo do músico **"Nando Reis"** para demonstrar como os filtros alteram o escopo da pesquisa.

### 1. Busca Padrão (Por Nome do Artista)
Retorna apenas os álbuns onde "Nando Reis" é o Artista Principal (neste caso, sua carreira solo).

`GET http://localhost:8889/v1/albums?artist=nando%20reis&limit=10&page=1`

```json
{
  "data": [
    {
      "albumId": "b390307b-5d3f-46e5-a854-2ff95f2f3783",
      "title": "Infernal",
      "artistId": "235cd710-c26c-4c89-bdeb-d45350b6d057",
      "artistName": "Nando Reis",
      "releaseDate": "2000-06-12",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T232033Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=107370adfc1b2798420f8e3a057b076202d3b4d3b1f62cf44711fe73a563de32",
        "type": "COVER"
      }
    }
  ],
  "pagination": {
    "totalPages": 1,
    "currentPage": 1,
    "itemsPerPage": 10,
    "totalIems": 1
  }
}
```

### 2. Busca em Participações Especiais (Feat)
Ao ativar a flag `search_in_guests`, a busca localiza álbuns de outros artistas onde Nando Reis aparece creditado como convidado em alguma faixa.  
`GET http://localhost:8889/v1/albums?artist=nando%20reis&limit=10&page=1&search_in_guests=true`

```json
{
  "data": [
    {
      "albumId": "cec811d0-b0f4-43c5-839a-3f5ba31476b5",
      "title": "Acústico MTV",
      "artistId": "9d179bee-c425-40af-a00e-999e82946009",
      "artistName": "Cássia Eller",
      "releaseDate": "2001-04-25",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T232323Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=418c9c665ddd28bde3a5acdb4f1150b55a1a5c824d632b9d9ddaa69d5b5615af",
        "type": "COVER"
      }
    },
    {
      "albumId": "b390307b-5d3f-46e5-a854-2ff95f2f3783",
      "title": "Infernal",
      "artistId": "235cd710-c26c-4c89-bdeb-d45350b6d057",
      "artistName": "Nando Reis",
      "releaseDate": "2000-06-12",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T232323Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=418c9c665ddd28bde3a5acdb4f1150b55a1a5c824d632b9d9ddaa69d5b5615af",
        "type": "COVER"
      }
    }
  ],
  "pagination": {
    "totalPages": 1,
    "currentPage": 1,
    "itemsPerPage": 10,
    "totalIems": 2
  }
}
```

### 3. Busca em Histórico de Membros (Lineups)
Ao ativar a flag `search_in_guests`, a API varre o histórico de formações das bandas. O sistema identifica que a Pessoa "Nando Reis" foi membro da banda "Titãs" no passado e retorna os álbuns daquela época específica.  
`GET http://localhost:8889/v1/albums?artist=nando%20reis&limit=10&page=1&search_in_guests=true`

```json
{
  "data": [
    {
      "albumId": "b390307b-5d3f-46e5-a854-2ff95f2f3783",
      "title": "Infernal",
      "artistId": "235cd710-c26c-4c89-bdeb-d45350b6d057",
      "artistName": "Nando Reis",
      "releaseDate": "2000-06-12",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T232712Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=cfb8c7051e1075bb931bdd4204eae89db9aa55b29735b4737153cf1bb013a216",
        "type": "COVER"
      }
    },
    {
      "albumId": "fb95219e-db42-4c57-92aa-dd7e91d2425a",
      "title": "Cabeça Dinossauro",
      "artistId": "e4e298ea-1dec-469a-86a4-3ded0c72d6d3",
      "artistName": "Titãs",
      "releaseDate": "1986-06-25",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T232712Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=cfb8c7051e1075bb931bdd4204eae89db9aa55b29735b4737153cf1bb013a216",
        "type": "COVER"
      }
    }
  ],
  "pagination": {
    "totalPages": 1,
    "currentPage": 1,
    "itemsPerPage": 10,
    "totalIems": 2
  }
}
```

### 4. Filtragem por ID de Artista (Catálogo Completo)
Este endpoint é ideal para carregar a página de detalhes de um artista ("Artist Page"). Ao passar o ID exato via `specific_artists`, a API ignora a busca textual e retorna a discografia completa vinculada àquele perfil, garantindo precisão total.

**Cenário:** O usuário acessou o perfil de **"Cássia Eller"** e o frontend precisa listar todos os seus álbuns.

`GET http://localhost:8889/v1/albums?limit=10&page=1&specific_artists=9d179bee-c425-40af-a00e-999e82946009`

```json
{
  "data": [
    {
      "albumId": "cec811d0-b0f4-43c5-839a-3f5ba31476b5",
      "title": "Acústico MTV",
      "artistId": "9d179bee-c425-40af-a00e-999e82946009",
      "artistName": "Cássia Eller",
      "releaseDate": "2001-04-25",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T233303Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7699c8bdd5d8a6edc40e7a0f2ef5f56b1abbfb058002ee7d53e9fd94f6cfa7f7",
        "type": "COVER"
      }
    },
    {
      "albumId": "c919433f-7ea2-4566-a0db-f0b5d940a499",
      "title": "Cássia Eller",
      "artistId": "9d179bee-c425-40af-a00e-999e82946009",
      "artistName": "Cássia Eller",
      "releaseDate": "1994-01-01",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T233303Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=7699c8bdd5d8a6edc40e7a0f2ef5f56b1abbfb058002ee7d53e9fd94f6cfa7f7",
        "type": "COVER"
      }
    }
  ],
  "pagination": {
    "totalPages": 1,
    "currentPage": 1,
    "itemsPerPage": 10,
    "totalIems": 2
  }
}
```

### 5. Combinação Avançada de Filtros (Interseção)
Todos os parâmetros de busca (`artist`, `specific_artists`, `search_in_guests`, `search_in_members`, `start_release_date`, etc.) podem ser combinados.  

**Cenário:** O usuário está navegando na discografia de **"Cássia Eller"** (`specific_artists`), mas deseja encontrar especificamente os álbuns onde **"Nando Reis"** (`artist`) fez uma participação especial (`search_in_guests`).

`GET http://localhost:8889/v1/albums?artist=nando%20reis&limit=10&page=1&search_in_guests=true&specific_artists=9d179bee-c425-40af-a00e-999e82946009`

**Resultado:** O sistema filtra os álbuns da Cássia e, dentro deles, busca onde Nando Reis aparece como convidado.

```json
{
  "data": [
    {
      "albumId": "cec811d0-b0f4-43c5-839a-3f5ba31476b5",
      "title": "Acústico MTV",
      "artistId": "9d179bee-c425-40af-a00e-999e82946009",
      "artistName": "Cássia Eller",
      "releaseDate": "2001-04-25",
      "tracks": 1,
      "image": {
        "url": "http://localhost:9000/uploads/albums/019c2ef9-311e-7ad5-8611-8d0693360e99.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=admin%2F20260205%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260205T233758Z&X-Amz-Expires=1800&X-Amz-SignedHeaders=host&X-Amz-Signature=18284858d5306c7549fe8435bef806c6fe549b11feb2757efbd5ce96cd13471b",
        "type": "COVER"
      }
    }
  ],
  "pagination": {
    "totalPages": 1,
    "currentPage": 1,
    "itemsPerPage": 10,
    "totalIems": 1
  }
}
```

## 📝 Notas Finais e Padrões da API

### 🔒 Segurança e Links de Imagens (Pre-signed URLs)
Para garantir a segurança dos ativos digitais, o bucket do MinIO está configurado como **Privado**.
* **Links Expiráveis:** Os URLs de imagens retornados pela API (e exibidos nos exemplos desta documentação) são **Pre-signed URLs** com validade temporária (TTL). Portanto, os links nos exemplos JSON acima estarão expirados no momento da leitura.
* **Acesso Controlado:** Tentar acessar um link após sua expiração ou remover os parâmetros de assinatura (`?X-Amz-Signature=...`) resultará em um erro `403 Forbidden` ou `Access Denied`. Isso comprova a implementação da política de segurança de arquivos (Private by Default).

### 📄 Paginação
Todos os endpoints de listagem e busca seguem um padrão estrito de paginação para garantir performance e previsibilidade.
A resposta da API encapsula os dados no campo `data` e fornece metadados de navegação no objeto `pagination`:

```json
{
  "data": [ 
    // Lista de Objetos (Álbuns, Artistas, etc...) 
  ],
  "pagination": {
    "totalPages": 5,
    "currentPage": 1,
    "itemsPerPage": 10,
    "totalItems": 48
  }
}
```
---

## ✅ Conclusão

Esta entrega representa uma solução **Cloud-Native**, pronta para escalar horizontalmente. A combinação de **DDD** para as regras de negócio com **Arquitetura Hexagonal** para a estrutura técnica permite que o software evolua em ambas as direções de forma independente e sustentável.