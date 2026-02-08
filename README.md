# Warrior Database Load Tester

Scala + Gatling load-testing project for Warrior API stress scenarios.

## Purpose
- Exercise Warrior API endpoints under increasing traffic and mixed request patterns.
- Surface latency/error behavior during create, lookup, and search-heavy workloads.

## Current Implementation
- Stack: Scala, Gradle, Gatling plugin (`io.gatling.gradle`).
- Main simulation: `src/gatling/scala/warriordatabase/WarriorDatabaseSimulation.scala`
- Scenario coverage includes:
- `POST /warrior` creation flow (with follow-up lookup by `Location` header)
- `GET /warrior?t=...` search flow
- invalid search request handling (`GET /warrior` expecting `400`)
- Payload/feed data files:
- `src/gatling/resources/warriors-payloads.tsv`
- `src/gatling/resources/search-terms.tsv`

## Target System Assumptions
- Default simulation base URL is `http://127.0.0.1:4000`.
- The API under test is expected to expose the Warrior routes above.

## Running the Tests
- Run Gatling simulation:
- `./gradlew gatlingRun`

## Current Status
- This repository is a focused load-testing harness, not the backend implementation itself.
- It is useful for repeatable traffic-pattern testing once the target API environment is available.

## Next Steps
- Add named simulation profiles (smoke, baseline, stress, spike).
- Export standardized reports and keep historical run artifacts in `docs/benchmarks/`.
- Parameterize base URL and credentials via environment variables for CI and staging use.
