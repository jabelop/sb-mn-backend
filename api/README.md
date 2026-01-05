# api
Api gateway for mn backend application.

## Features
The api gateway connects with the services through RabbitMQ queues.
You can get the swagger documentation running the api and browsing the `/swagger` path.
Swagger does not allow to call get endpoints with body, in order to test these enpoints you can launch them from swagger,
then copy the curl command displayed to a terminal and get the results running that command.
There is no issue with post or put endpoints.

Here's a list of features included in this project:

| Name                      | Description                                                                                     
|---------------------------|------------------------------------------------------------------------------------------------|
| Query all the players   | Retrieves all the players created                                                                |
| Create one player | Creates one player                                                                                     |
| Query all the creatures    | Retrieves all the creatures created                                                           |
| Create one player creature | Creates one player creature                                                                   |
| Query all the players creatures | Retrieves all the player creatures for the given parameters                              |
| Update player creature  | Updates the xp of a player creature increasing the other needed properties, starting by the level|
| Query all the combats                  | Retrieves all the combats created                                                 |
| Create one combat                      | Creates one combat for two existing players                                       |
| Query all the players combat data      | Retrieves all the combat data for the given parameters                            |

## Building & Running
The service needs to have the RabbitMQ instance and the PostgreSQL data base one, running.
You can take a look of `resources/application.yaml` file at the root of the application project to get the credentials needed.
The service runs standalone but in order to get the full application working you have to get all the services running.
Take a look of the README at the root of the application project to get the step by step guide to get up the full application.
After those steps the service should be full operating.

To build or run the project, use one of the following tasks:

| Task                                    | Description                                                       |
| -----------------------------------------|------------------------------------------------------------------ |
| `./gradlew test`                        | Run the tests                                                     |
| `./gradlew build`                       | Build everything                                                  |
| `./gradlew buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `./gradlew run`                         | Run the server                                                    |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```
