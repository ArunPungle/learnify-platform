# Learnify Platform — Makefile

.PHONY: up down build logs clean restart ps

## Start all services
up:
	docker-compose up -d

## Stop all services
down:
	docker-compose down

## Build all images
build:
	docker-compose build --no-cache

## Start only infrastructure (DBs, Redis, Kafka, Zipkin)
infra:
	docker-compose up -d postgres-auth postgres-user postgres-course \
		postgres-lesson postgres-enrollment postgres-review \
		postgres-payment postgres-analytics redis zookeeper kafka kafka-ui zipkin

## View all logs
logs:
	docker-compose logs -f

## View logs for specific service (usage: make log s=auth-service)
log:
	docker-compose logs -f $(s)

## Remove containers and volumes
clean:
	docker-compose down -v --remove-orphans

## Restart a service (usage: make restart s=course-service)
restart:
	docker-compose restart $(s)

## Show running containers
ps:
	docker-compose ps

## Run all tests
test:
	mvn test

## Build all Maven modules (skip tests)
package:
	mvn clean package -DskipTests
