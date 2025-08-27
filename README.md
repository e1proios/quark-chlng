# quark-chlng
this project consists of two Quarkus microservices, each run in a separate docker container:
- `invoice-scanner-service`
- `iban-blacklist-service`

additionally, following services are run in their own docker containers:
- a mongodb instance
- a kafka server
- a file server

## prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Java 17+

## build & run

in project root directory, first run
<br>
```$ gradle build```

and then
<br>
```$ docker compose up --build```

## test

### curl

### kafka

start two shells, connect to kafka server in both
<br>
```$ docker exec -it franz-kafka bash```

start a message consumer in one of them
<br>
```$ /opt/kafka/bin/kafka-console-consumer.sh --topic processed-invoices --bootstrap-server franz-kafka:9092```

start a message producer in the other one
<br>
```$ /opt/kafka/bin/kafka-console-producer.sh --topic invoice-urls --bootstrap-server franz-kafka:9092```

enter the URL of the testing PDF into the producer interactive session
<br>
```http://file-server:3333/Testdata_Invoices.pdf```

see the response – a serialized `PdfInfoMessage` – in the consumer terminal
