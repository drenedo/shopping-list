# Shopping list

This project is under development.

## What is it?

This app could help you if you need to manage shopping lists.

## Who it works?

The app needs java 17 and access to some database.

## Build

Build for use the backend with MariaDB

```shell
./gradlew bootJar -Pmariadb
```

Build for use the backend with H2

```shell
./gradlew bootJar
```

## Notes and shortcuts

### Make a petition
```shell
curl -d '{"name":"item"}' -H "Content-Type: application/json" -X PUT http://localhost:8080/api/v1/current/lala
```
