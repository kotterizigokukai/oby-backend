# Google Sign-In Integration

This document describes how to configure and use the Google OAuth 2.0 login flow that powers authentication in the backend.

## Overview

- Google is the only external identity provider. Users authenticate via OAuth 2.0 / OpenID Connect.
- Spring Security manages the authentication flow, sessions, and CSRF protection.
- User profiles (email, display name, avatar, login timestamps) are persisted in PostgreSQL for ID management.
- No Google access or refresh tokens are stored server-side; only profile metadata is retained.

## Prerequisites

1. **Google Cloud Project**
   - Create or reuse a Google Cloud project.
   - Enable the *Google Identity Services (OAuth 2.0)* API.
2. **OAuth Consent Screen**
   - Publish the consent screen (external type is required for production).
   - Add the application scopes `openid`, `profile`, `email`.
3. **OAuth Client Credentials**
   - Create a *Web application* OAuth client ID.
   - Add the authorized redirect URI: `http://localhost:8080/login/oauth2/code/google` (adjust host/port for other environments).
   - Record the generated **Client ID** and **Client secret**.

## Environment Variables

Populate `.env` (based on `.env.example`) with the values above:

```dotenv
GOOGLE_CLIENT_ID=xxxxxxx.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=super-secret
SERVER_SESSION_COOKIE_SECURE=false  # set true when HTTPS is enforced
```

> `SERVER_SESSION_COOKIE_SECURE=true` must be used in production to ensure cookies are sent only over HTTPS.

The application reads these variables via `spring.security.oauth2.client.*` properties in `application.properties`.

## Running Locally

1. Export the environment variables or create a `.env` file.
2. Start PostgreSQL (use `docker-compose up db` if you rely on the provided Compose file).
3. Run the application:

```bash
./gradlew bootRun
```

4. Open `http://localhost:8080/oauth2/authorization/google`. After successful login you will be redirected to `/auth/me`, which returns the authenticated profile.

## Security Considerations

- CSRF protection is enforced with cookie-based tokens to support browser clients.
- Sessions use same-site, HTTP-only cookies. Toggle `SERVER_SESSION_COOKIE_SECURE` per environment.
- Session fixation is mitigated (`migrateSession`) and a `HttpSessionEventPublisher` is registered for concurrency controls.
- Stored profile data is limited to the least information necessary for ID management. Google tokens are never persisted.
- `/api/messages` `GET` endpoints are public for read access; creating messages requires authentication and the `ROLE_USER` authority.

## Relevant Endpoints

- `GET /oauth2/authorization/google` – starts the Google login flow.
- `GET /auth/me` – returns the authenticated user's profile (requires login).
- `POST /auth/logout` – handled via Spring Security logout, returns HTTP 204 (requires CSRF token).
- `GET /api/messages` – public, read messages.
- `POST /api/messages` – protected; requires `ROLE_USER`.

## Database Schema

`schema.sql` creates two tables:

- `app_user`: stores Google identity linkage and profile metadata.
- `message`: simple demo data used by `MessageController`.

The schema is applied automatically on startup via Spring's SQL initialization.
