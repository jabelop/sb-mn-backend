# combats
Combats service for mn backend application.

## Features
The service connects with the api gateway through RabbitMQ queues.

Here's a list of features included in this project:

| Name                                                                          | Description                                                                        |
|-------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| Query all the combats                                                         | Retrieves all the combats created                                                  |
| Create one combat                                                             | Creates one combat for two existing players                                        |
| Query all the players combat data                                             | Retrieves all the combat data for the given parameters                             |
| Run a full combat against a AI player (support for player vs player incoming) | Runs a full combat betwen a player and an AI player                                |

## Building & Running
The service needs to have the RabbitMQ instance and the PostgreSQL data base one, running. 
You can take a look of `resources/application.yaml` file at the root of the application project to get the credentials needed.
The service runs standalone but in order to get the full application working you have to get all the services running.
Take a look of the README at the root of the application project to get the step by step guide to get up the full application.
Once you have all running you can run the Migrations file from the `peristence/migrations` folder to get the data base initialized.
After those steps the service should be full operating.

In order to run a combat, a combat simulator has been created, browsing to `http://URL_COMBATS_SERVICE:PORT_COMBATS_SERVICE/static` will send you
an HTML page where you can create a combat providing two player's id (remember the second player must be the AI one, created on the player service's 
migrations until the player vs player combat support is implemented). Then you can click the run combat button to start the combat, the screen will be
updated and you will be able to play against the machine. A screen shot with this screen is provided at the root of the application project.
All this process runs over Websockets so real time communication is under the hood of the combat simulation.

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
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8081
```

