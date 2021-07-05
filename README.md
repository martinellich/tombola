# Tombola App

## Run PostgreSQL

    docker run --name tombola-postgres -p5432:5432 -e POSTGRES_DB=tombola -e POSTGRES_USER=tombola -e POSTGRES_PASSWORD=tombola -d postgres
