# repo: quark-chlng

---
## prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Java 17+

---
## architecture
this project consists of two connected Quarkus microservices:
- `invoice-scanner-service`
- `iban-blacklist-service`

additionally, following supporting services are run in a docker network:
- a mongodb instance
- a kafka server
- a file server
- a keycloak authentication server with its own postgres DB

---
## build & run
1. run `$ gradle build` in the root directory
2. run `$ docker compose up --build` in the root directory
3. run `$ quarkus dev` in `./invoice-scanner-service`
4. run `$ quarkus dev` in `./iban-blacklist-service`

---
## test

### keycloak users
- adolf: adolf
- bogdan: bogdan
- charlemagne: vivelafrance

bogdan can blacklist
charlemagne can scan
adolf can do whatever he wants

---
### curl

---
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
