# Comment Tree Backend

## Quick Start

### Maven

Java version 11+ is required.

```
./mvnw spring-boot:run
```

### Docker
```
docker run --rm --name comments-tree-backend \
            -p 8080:8080 \
        wangding85/comments-tree-backend
```
**or use docker-compose to start frontend and backend with single command**

```
docker-compose up
```

## Architecture
Based on Spring Boot, the backend is a RESTful API service.

- Data Layer
  - Using H2 as the database without installation.

- ORM
  - Despite the `spring-data-jpa` is quite popular for a small project, I still prefer `MyBatis` because the SQL write by my hands is not auto generated.

- Init & Upgrade
  - `Flyway` is a good tool to manage database migration.

- Security
  - `spring-security` provides out-of-the-box security, but it needs some customization because of the separation of front-end and back-end. 
  - The authentication chain like this:`AuthTokenFilter` check the credentials in session and cookie → `AuthUserDetailsServiceImpl` (implements the `UserDetailsService`) → `AccountService#login` → if found account then use `UsernamePasswordAuthenticationToken` to check password is correct or not.
  - Session: Once the user logged in, set the `SecurityContext` into session manually, and the `JSESSION` will send back to the client 
  - Cookie based JWT: If user logged in with `remember-me`, will create a `JWT` token and send it to the client as a cookie value.

- Exception Handling
  - Many projects like to wrap all responses (even if error) with 200 status. In this homework I tried to use native HTTP code, 4xx means client error, 5xx means service error, and I wrapped them all in `ApiError`. I think there is no right or wrong between the former and the latter. Just choose one and reach a team consensus.

- Service Layer
  - Due to this homework being simple, I choose an anemic domain model to build up the logic layer.

- Entity ↔ DTO Mapper
  - DTO is exposed to the web layer (controller) meanwhile entity is controlled behind the service layer. The mapper process will lose some performance, but it's useful to prevent mixed presentation properties from the persistence model.

- Web Layer
  - `JSR-303` is an effective standard for validating, but the Exception is hard to manipulate, therefore, still write lots of code to validate user input arguments.

## Known Issues
  - Cookie without the `Secure` attribute, because there's no enough time to debug over HTTPS.
  - Performance issue: JOIN `account` table to query comments to get usernames
    - If the account data is huge or allows lazy load comments data, I would like to separate it as two queries and assemble result in the program.
  - The `depth` filed in `comment` is not necessary for this homework, but it would be useful when lazy loading.
  - The JVM parameters is not being tuning