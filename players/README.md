# players
Players service for mn backend application.

## Features
The service connects with the api gateway through RabbitMQ queues.

Here's a list of features included in this project:

| Name                      | Description                                                                                   |
|---------------------------|-----------------------------------------------------------------------------------------------|
| Query all the players   | Retrieves all the players created                                                           |
| Create one player | Creates one player                                                                    |

## Building & Running
The service needs to have the RabbitMQ instance and the PostgreSQL data base one, running.
You can take a look of `resources/application.yaml` file at the root of the application project to get the credentials needed.
The service runs standalone but in order to get the full application working you have to get all the services running.
Take a look of the README at the root of the application project to get the step by step guide to get up the full application.
Once you have all running you can run the Migrations file from the `peristence/migrations` folder to get the data base initialized.
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
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8082
```
