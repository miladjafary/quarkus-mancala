# Mancala Game

Mancala is a game in the mancala family invented in the United States by William Julius Champion, Jr. in 1940. 
The general rules of the game are explained on Wikipedia(https://en.wikipedia.org/wiki/Kalah) and this game is runs a game of 6-stone Kalah. 

This project has been developed on Quarkus, which is a Supersonic Subatomic Java Framework. 
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
mvn package
```
It produces the `mancala-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.

The application is now runnable using `java -jar target/mancala-1.0.0-SNAPSHOT-runner.jar`.

# Mancala REST API

There are some REST api in this project to manage the game. You can find out the API specification from the
`src/main/resources/META-INF/openapi.yml`

Besides, once your application is started, you can make a request to the default `/openapi` endpoint to download the API specification
```
curl http://localhost:8080/openapi
```

## View REST API in Swagger UI
You can access to the swaggerui when the project is started in dev mode. 
```shell script
mvn compile quarkus:dev
```
Once your application is started, you can go to [http://localhost:8080/swagger-ui]() and play with your API.

#Game SDK
In this project a Mancala game sdk has been developed that is responsible for preparation and rule controlling of the game. 
Since it has been developed based on a pure java, you can take advantage of it to use in any other Java framework such as Spring Boot.

The SDK can be found under the `src/main/java/com/miladjafari/mancala/sdk` package