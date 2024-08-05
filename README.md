<center>
  <p align="center">
    <img src="https://icon-library.com/images/java-icon-png/java-icon-png-15.jpg"  width="150" />
  </p>  
  <h1 align="center">🚀 Microserviço: Admin do Catálogo de Vídeos com Java</h1>
  <p align="center">
    Microserviço referente ao backend da Administração do Catálogo de Vídeos<br />
    Utilizando Clean Architecture, DDD, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Ferramentas necessárias

- JDK 17
- IDE de sua preferência
- Docker

## Como executar?

1. Clonar o repositório:
```sh
git clone git@github.com:Nesrux/admin-do-catalogo.git
```

2. Subir o banco de dados MySQL com Docker:
```shell
docker-compose up -d
```

3. Executar as migrações do MySQL com o Flyway:
```shell
./gradlew flywayMigrate
```

4. Executar a aplicação como SpringBoot app:
```shell
./gradlew bootRun
``` 

> Também é possível executar como uma aplicação Java através do
> método main() na classe Main.java
## Banco de dados

O banco de dados principal é um MySQL e para subir localmente vamos utilizar o
Docker. Execute o comando a seguir para subir o MySQL:

```shell
docker-compose up -d
```

Pronto! Aguarde que em instantes o MySQL irá estar pronto para ser consumido
na porta 3306.

### Migrações do banco de dados com Flyway

#### Executar as migrações

Caso seja a primeira vez que esteja subindo o banco de dados, é necessário
executar as migrações SQL com a ferramenta `flyway`.
Execute o comando a seguir para executar as migrações:

```shell
./gradlew flywayMigrate
```

Pronto! Agora sim o banco de dados MySQL está pronto para ser utilizado.

<br/>

#### Limpar as migrações do banco

É possível limpar (deletar todas as tabelas) seu banco de dados, basta
executar o seguinte comando:

```shell
./gradlew flywayClean
```

MAS lembre-se: "Grandes poderes, vem grandes responsabilidades".

<br/>

#### Reparando as migrações do banco

Existe duas maneiras de gerar uma inconsistência no Flyway deixando ele no estado de reparação:

1. Algum arquivo SQL de migração com erro;
2. Algum arquivo de migração já aplicado foi alterado (modificando o `checksum`).

Quando isso acontecer o flyway ficará em um estado de reparação
com um registro na tabela `flyway_schema_history` com erro (`sucesso = 0`).

Para executar a reparação, corrija os arquivos e execute:
```shell
./gradlew flywayRepair
```

Com o comando acima o Flyway limpará os registros com erro da tabela `flyway_schema_history`,
na sequência execute o comando FlywayMigrate para tentar migrar-los novamente.

<br/>

#### Outros comandos úteis do Flyway

Além dos comandos já exibidos, temos alguns outros muito úteis como o info e o validate:

```shell
./gradlew flywayInfo
./gradlew flywayValidate
```

Para saber todos os comandos disponíveis: [Flyway Gradle Plugin](https://flywaydb.org/documentation/usage/gradle/info)

<br/>

#### Para executar os comandos em outro ambiente

Lá no `build.gradle` configuramos o Flyway para lêr primeiro as variáveis de
ambiente `FLYWAY_DB`, `FLYWAY_USER` e `FLYWAY_PASS` e depois usar um valor padrão
caso não as encontre. Com isso, para apontar para outro ambiente basta sobrescrever
essas variáveis na hora de executar os comandos, exemplo:

```shell
FLYWAY_DB=jdbc:mysql://prod:3306/adm_videos FLYWAY_USER=root FLYWAY_PASS=123h1hu ./gradlew flywayValidate
```

### Executando com Docker
Para rodar a aplicação localmente com Docker, iremos utilizar o `docker compose` e necessita de apenas três passos:
<br/>

#### 1. Gerando o artefato produtivo (jar)

Para gerar o artefato produtivo, basta executar o comando:
```
./gradlew bootJar
```
<br/>

#### 2. Executando os containers independentes

Para executar o MySQL e o Rabbit, basta executar o comando abaixo.
```
docker-compose up -d
```
<br/>

#### 3. Executando a aplicação junto dos outros containers

Depois de visualizar que os demais containers estão de pé, para rodar sua aplicação junto basta executar o comando:
```
docker-compose --profile app up -d
```

> **Obs.:** Caso necessite rebuildar a imagem de sua aplicação é necessário um comando adicional:
>```
>docker compose build --no-cache app
>```

#### Parando os containers

Para encerrar os containers, basta executar o comando:
```
docker compose --profile app stop
```

### Keycloak

#### Setup

1. Adicionar no docker-compose o container do Keycloak
    ```
      keycloak:
        container_name: adm_videos_keycloak
        image: quay.io/keycloak/keycloak:20.0.3
        environment:
          - KEYCLOAK_ADMIN=admin
          - KEYCLOAK_ADMIN_PASSWORD=admin
        ports:
          - 8443:8080
        command:
          - start-dev
    ```
2. Subir o container e navegar ate `http://localhost:8443/`
3. Criar um realm novo para o projeto: `fc3-codeflix`
4. Navegar ate Realm settings > General > Endpoints
    - Esses endpoints são importantes para fazer-mos a integração
5. Navegar ate Realm settings > Keys
    - Iremos utilizar a chave publica do algoritmo RS256 para verificar o token
6. Criar o client:
    - Client Id: fc3-admin-catalogo-de-videos
    - Client authentication: ON -- isso faz acesso confidential
    - Redirect URL: confidential
    - Comentar das credentials `client and secret` que usaremos para login manual
7. Criar a role:
    - Role: catalogo-admin
    - Description: Role que dá permissão de admin para os usuários
8. Criar um group:
    - Name: catalogo-admin
    - Role mapping: assign `catalogo-admin`
9. Criar um usuario:
    - Nome: myuser
    - Groups: adicionar ao `catalogo-admin`
    - Criar um credentials: `123456`
10. Criar o client para o frontend:
   - Client Id: react-auth
   - Client authentication: OFF -- isso faz acesso publico
   - Root URL: `http://localhost:3000`
   - Valid redirect URIs: `http://localhost:3000/*`   -- É necessário o /* 
   - Web origins: `http://localhost:3000`  -- Essa propriedade evita bloqueio de CORS
   - Realm Settings -> Security Defenses -> Content-Security-Policy: `frame-src 'self'; frame-ancestors 'self' http://localhost:3000;`

#### Integration

1. Adicionar o starter do spring boot:
   ```
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    
    testImplementation('org.springframework.security:spring-security-test')
   ```
2. Configuração das properties:
   ```properties
       keycloak:
           realm: fc3-codeflix
           host: http://localhost:8443
      
       spring:
           security:
               oauth2:
                   resourceserver:
                       jwt:
                           jwk-set-uri: ${keycloak.host}/realms/${keycloak.realm}/protocol/openid-connect/certs
                           issuer-uri: ${keycloak.host}/realms/${keycloak.realm}
   ```