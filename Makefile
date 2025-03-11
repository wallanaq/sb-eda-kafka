.PHONY: all down build image up restart

all: restart

down:
	docker compose -f compose.yaml down -v

build:
	mvn clean package -DskipTests

image:
	docker rmi wallanaq/sb-eda-kafka:0.0.1
	docker build -t wallanaq/sb-eda-kafka:0.0.1 .

up:
	docker compose -f compose.yaml up -d

restart: down build image up
