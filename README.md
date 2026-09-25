# URL Shortener — VS Code Ready

A self-contained URL shortener built with **Java 21**, **Spring Boot 3.5.16**, Spring Data JPA and H2.

## Requirements

- JDK 21 or newer (JDK 25 is also supported by Spring Boot 3.5.16)
- VS Code + Extension Pack for Java
- No PostgreSQL and no Docker are required for the local run.

## Run in VS Code

Open this folder in VS Code, then run:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Or use the Run button above `main()` in `UrlShorteningServiceApplication.java`.

Application: http://localhost:8080
H2 Console: http://localhost:8080/h2-console

H2 JDBC URL:
`jdbc:h2:file:./data/urlshortener`

Username: `sa`
Password: empty

## Web UI

Open `http://localhost:8080/` in your browser for the built-in URL shortener interface. Enter a URL, click **Shorten URL**, and copy the generated short link.

## API

### Create short URL

```http
POST http://localhost:8080/generate
Content-Type: application/json

{
  "url": "https://www.google.com",
  "expirationDate": "2026-12-31T23:59:59"
}
```

`expirationDate` is optional. Without it, the link expires after 10 minutes.

Example response:

```json
{
  "originalUrl": "https://www.google.com",
  "shortLink": "a1b2c3d4",
  "expirationDate": "2026-12-31T23:59:59"
}
```

### Redirect

Open:

```text
http://localhost:8080/a1b2c3d4
```

The API responds with HTTP 302 and redirects to the original URL.

## Why this version is different from the original

- Upgraded Spring Boot from the obsolete 2.3.4 release to 3.5.16.
- Uses `jakarta.*` APIs required by Spring Boot 3.
- Uses Java 21 while remaining runnable on newer supported JDKs.
- Removed unnecessary Guava and Commons Lang dependencies.
- Removed PostgreSQL/Docker dependency for the basic local application.
- Uses H2 file storage so data survives application restarts.
- Replaced field injection with constructor injection.
- Added request validation and centralized error handling.
- Fixed redirect handling so it returns a proper HTTP 302 response.
- Fixed HTTP status codes for missing/expired URLs.
- Added automated Spring Boot tests.
- Added VS Code configuration and a simple run script.
