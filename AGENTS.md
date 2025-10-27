# Repository Guidelines

## Project Structure & Module Organization
- Kotlin sources live in `src/main/kotlin/com/example/obybackend`; keep new features grouped by domain (e.g., `message`, `user`) using nested packages.
- HTTP controllers, services, and repositories stay separated (`*Controller`, `*Service`, `*Repository`) to match the current layering; shared DTOs sit alongside their feature.
- Static resources or configuration go under `src/main/resources`; seed data belongs in `resources/db`.
- Tests mirror production code in `src/test/kotlin`, matching package paths (e.g., `.../message/MessageServiceTest.kt`).
- Reference architecture notes or diagrams belong in `docs/`; API probing examples live in `requests.http`.

## Build, Test, and Development Commands
- `./gradlew bootRun`: launches the Spring Boot app at `http://localhost:8080`.
- `./gradlew build`: full compile plus unit tests; use before pushing.
- `./gradlew test`: runs the JUnit 5 suite; add `--info` for verbose logs.
- `./gradlew ktlintCheck`: enforces Kotlin formatting; pair with `ktlintFormat` to auto-fix.
- `docker-compose up -d`: starts the PostgreSQL service defined in `docker-compose.yml`; use `docker-compose logs postgres` for troubleshooting.

## Coding Style & Naming Conventions
- Kotlin 1.9 with Spring Boot 3; prefer top-level immutable vals and constructor injection.
- Use 4-space indentation, `UpperCamelCase` for classes, `lowerCamelCase` for functions/properties, and suffix interfaces with `Port` only when they abstract infrastructure.
- Spring MVC endpoints adopt `@RequestMapping("/api/...")`; keep request/response models in `dto` packages.
- Always run `./gradlew ktlintFormat` before committing to avoid CI failures.

## Testing Guidelines
- Write unit tests with JUnit 5 and Spring Boot’s test starter; name files `*Test.kt` and methods using backtick-friendly descriptions (e.g., ``fun `returns 404 when message missing`()``).
- Include integration tests when touching persistence; leverage `@SpringBootTest` with testcontainers if DB interaction is required.
- Target meaningful branch coverage for new logic and document any intentional gaps in the PR.

## Commit & Pull Request Guidelines
- Follow the existing history: concise, imperative subject lines (Japanese or English) such as `Add message endpoint` or `メッセージ一覧APIを作成`.
- Reference issue numbers with `(#123)` when applicable and keep bodies under 72 characters per line.
- Pull requests should explain the change, outline validation commands, and attach screenshots for UI-facing APIs (e.g., Swagger diffs).
- Ensure CI passes locally (`build`, `test`, `ktlintCheck`) before requesting review; link related docs (`docs/`) or query samples (`requests.http`) to aid reviewers.



### Rule ###
- 回答は**日本語**で簡潔かつ丁寧にしてください
- ディレクトリやあファイルを作るときはクリーンアーキテクチャに則った形式で作ってください
- プログラムにはコメントを書きわかりやすくしてください
- プログラムを作成する際は必ず一度確認を入れてください
