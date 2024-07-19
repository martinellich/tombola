# Tombola App

## Run PostgreSQL

    docker run --name tombola-postgres -p5435:5432 -e POSTGRES_DB=tombola -e POSTGRES_USER=tombola -e POSTGRES_PASSWORD=tombola -d postgres
