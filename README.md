# repo: quark-chlng

## prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Java 17+

## architecture
this project consists of two connected Quarkus microservices:
- `invoice-scanner-service`
- `iban-blacklist-service`

additionally, following supporting services are run in a docker network:
- a mongodb instance
- a kafka server
- a file server
- a keycloak authentication server with its own postgres DB

## build & run
1. run `$ gradle build` in the root directory
2. run `$ docker compose up --build` in the root directory
3. run `$ quarkus dev -Ddebug=5005` in `./invoice-scanner-service`
4. run `$ quarkus dev -Ddebug=5006` in `./iban-blacklist-service`

## test

### keycloak users

#### chief
user: chief\
pwd: chief\
rights: [admin]

#### adolf
user: adolf\
pwd: adolf\
rights: [blacklist, scan]

#### bogdan
user: bogdan\
pwd: bogdan\
rights: [blacklist]

#### charlemagne
user: charlemagne\
pwd: vivelafrance\
rights: [scan]

### test API w/ curl

#### get access token

replace client id + client secret and user name + password based on which endpoint you want to test

```
curl --request POST \
    --url 'http://localhost:8888/realms/quark/protocol/openid-connect/token' \
    --header 'Content-Type: application/x-www-form-urlencoded' \
    --data 'client_id=invoice-scanner-service' \
    --data 'client_secret=nm1Hv5Z6zHbkrYtzGJwlQBbly3mzBzlq' \
    --data 'username=adolf' \
    --data 'password=adolf' \
    --data 'grant_type=password'
```

optionally pipe your favorite json formatting tool for redable output

```
curl --request POST \
--url 'http://localhost:8888/realms/quark/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data 'client_id=invoice-scanner-service' \
--data 'client_secret=nm1Hv5Z6zHbkrYtzGJwlQBbly3mzBzlq' \
--data 'username=adolf' \
--data 'password=adolf' \
--data 'grant_type=password' | json_pp
```

#### assign the token to a shell var

```TKN={ACCESS_TOKEN_FROM_PREVIOUS_CALL}```

#### ping the test endpoint
```
curl --request GET \
    --url "http://localhost:8080/api/ping" \
    -H "Authorization: Bearer $TKN"
```

#### request a PDF scan
```
curl --request POST \
    --url "http://localhost:8080/api/scan" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TKN" \
    --data '{"url":"http://localhost:3333/Testdata_Invoices.pdf"}'
```

optionally pipe a json formatting tool 

```
curl --request POST \
    --url "http://localhost:8080/api/scan" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TKN" \
    --data '{"url":"http://localhost:3333/Testdata_Invoices.pdf"}' | json_pp
```

---

### kafka

start two shells, connect to kafka server in both

```$ docker exec -it franz-kafka bash```

start a message producer in one of them

```$ /opt/kafka/bin/kafka-console-producer.sh --topic invoice-urls --bootstrap-server franz-kafka:9092```

start a message consumer in the other one

```$ /opt/kafka/bin/kafka-console-consumer.sh --topic processed-invoices --bootstrap-server franz-kafka:9092```

enter the URL of the testing PDF into the producer interactive session

```http://localhost:3333/Testdata_Invoices.pdf```

see the response – a serialized `PdfInfoMessage` – in the consumer terminal
