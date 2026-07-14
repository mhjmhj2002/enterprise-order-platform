# Event Platform Local

Infraestrutura local da Sprint 2 para Kafka em KRaft e Kafka UI. Ela nao inicia servicos de negocio.

## Subir a plataforma

```bash
docker compose -f infra/event-platform/compose.yaml up -d
```

Kafka fica disponivel para aplicacoes executadas no host em `localhost:9094`. A Kafka UI fica em `http://localhost:8080`.

## Validar

```bash
docker compose -f infra/event-platform/compose.yaml ps
docker compose -f infra/event-platform/compose.yaml exec kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server kafka:9092 --describe --topic mercadoaurora.order.order-confirmed.v1
curl http://localhost:8080/actuator/health
```

O topico provisionado possui uma particao, fator de replicacao 1, limpeza `delete` e retencao de sete dias. O volume nomeado `kafka-data` preserva os dados do broker entre reinicializacoes. O broker usa o usuario root somente no ambiente local para inicializar esse volume nomeado com a imagem oficial.

## Profile Spring

O Order Service publica eventos somente quando executado com o profile Spring `kafka`:

```bash
cd services/order-service
mvn spring-boot:run -Dspring-boot.run.profiles=kafka
```

Use `KAFKA_BOOTSTRAP_SERVERS` para substituir o default `localhost:9094`. O Inventory Service ainda nao possui consumer nesta Story.
