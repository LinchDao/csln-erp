# Repository Guidelines

## Project Structure & Module Organization
This repository is a Spring Boot 3 + MyBatis-Plus ERP backend.

- `src/main/java/com/lin/csln`: application code, organized by layer:
  - `controller` (HTTP endpoints)
  - `service` and `service/impl` (business logic)
  - `mapper` (MyBatis mapper interfaces)
  - `entity`, `dto`, `enums`, `config`, `common`, `utils`
- `src/main/resources`:
  - `application.yaml` (runtime config)
  - `mapper/*.xml` (SQL mappings)
  - `static`, `templates` (web assets/templates)
- `src/test/java`: tests (currently includes `CslnErpApplicationTests`).
- `docs` and `data`: project docs and local data artifacts.

## Build, Test, and Development Commands
Use Java 17 and Maven wrapper from repo root.

- `./mvnw clean compile` (or `mvnw.cmd clean compile` on Windows): compile sources.
- `./mvnw test`: run unit/integration tests.
- `./mvnw spring-boot:run`: start app in local dev mode.
- `./mvnw clean package -DskipTests`: build runnable artifact quickly.

## Coding Style & Naming Conventions
- Follow layered package structure; do not mix controller/service/mapper concerns.
- Java style: 4-space indentation, UTF-8 encoding, LF line endings (`.editorconfig`).
- Class naming:
  - `*Controller`, `*Service`, `*ServiceImpl`, `*Mapper`, `*DO`, `*DTO`.
- Keep transaction rules explicit:
  - service classes default to read-only;
  - write methods must use `@Transactional(rollbackFor = Exception.class)`.

## Chinese Encoding Safety (AI Editing Rules)
- All newly created or modified text files must use `UTF-8` (no BOM) unless a file already has a different required encoding.
- Do not change existing line-ending policy; keep LF for source/text files and CRLF only where explicitly configured.
- When reading/writing Chinese content on Windows PowerShell, use explicit UTF-8 commands/options to avoid mojibake.
- If garbled characters are detected, stop editing that file and re-open/re-read it with explicit UTF-8 before making any change.
- Prefer minimal edits and avoid unnecessary rewrites of large files containing Chinese text.

## Testing Guidelines
- Framework: Spring Boot Test (`spring-boot-starter-test`).
- Place tests under `src/test/java` mirroring production package paths.
- Naming: `*Tests` for Spring context tests, `*Test` for focused unit tests.
- Minimum expectation for new features:
  - service-level logic test for core branch/validation;
  - mapper/API integration test when SQL or contract changes.

## Commit & Pull Request Guidelines
Recent history uses concise type-prefixed messages, e.g.:
- `feat：采购单功能`
- `fix：代码调整`

Recommended format: `<type>：<summary>` where `type` is `feat`, `fix`, `refactor`, `test`, or `docs`.

For PRs, include:
- clear scope and why the change is needed;
- impacted modules/files;
- API/DB/config changes and rollback notes;
- test evidence (commands run and results).
