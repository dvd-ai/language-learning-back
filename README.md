# Language Learning Back

## Table of Contents

- [Description](#description)
- [What I've learned](#what-ive-learned)
- [Prerequisites ](#prerequisites)
- [How to use](#how-to-use)

## Description
- REST API for generating a dictionary (words, phrases) on any topic entered by users. Currently maintains 2 target languages (EN, DE).
- The generation succeds using external OPENAI API (ChatGpt models)

### Tech Stack
- Spring Framework (Boot, REST, Data JPA, AI)
- Docker
- Flyway
- Swagger
- PostgreSQL


## What I've learned
- integrating external AI APIs in the app
- implementing multiple asynchronous calls to the external API using Compleatable Future and joining the responses from the API
- mapping JSON data in PostgreSQL with Spring Data JPA

## Prerequisites
- Java 17
- Docker (optional)
- OPENAI API key

## How to use
- clone the project
- edit application properties: put your datasource configuration, the API key or use a docker compose to configure the file at start up. The app can be used with a front app (if you specify the url)
- use "dev" profile to get the overview of how the app works (with saving money) or use "prod" to see the full functionality.
- run the project with 
- build the project with **gradle :bootJar** and launch the project with **docker-compose up** or **gradlew bootRun** if you don't use docker



