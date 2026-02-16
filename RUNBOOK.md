# Production Runbook

## Health checks
- `GET /actuator/health` on each Spring service (ensure actuator is enabled in environment profile)
- PostgreSQL: `pg_isready`

## Startup order
1. PostgreSQL
2. Kafka + Zookeeper
3. billing-service
4. patient-service + analytics-service
5. api-gateway

## Pre-deploy checklist
- Verify database credentials and Kafka bootstrap URL env vars
- Confirm service-to-service DNS names resolve inside network
- Run CI workflow (`mvn clean verify` per module)
- Validate compose config: `docker compose config`

## Rollback
- Redeploy previous container images for affected services
- Revert config/env changes
- For schema-related problems, restore from latest DB backup before redeploy
