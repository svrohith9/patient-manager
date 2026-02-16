# Contributing

Thanks for contributing to Patient Manager.

## Setup

- Install Java 21+, Maven, Docker.
- Start dependencies with `docker compose -f docker-compose.yaml up -d`.
- Run/edit services independently.

## Development guidelines

- Keep PRs scoped (prefer one service per PR when possible)
- Add/update docs for endpoint or config changes
- Avoid breaking API contracts without explicit note

## Validation before PR

Run tests in touched modules:

```bash
./mvnw test
# or
mvn test
```

## Pull request checklist

- [ ] Problem and solution explained
- [ ] Tests/builds run for touched modules
- [ ] API/config docs updated
- [ ] No secrets committed
