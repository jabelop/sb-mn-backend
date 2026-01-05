
# mn backend
Mini backend for multiplayer RPG game

## Features
The application is implemented with the microservices architecture, having an api gateway and services for players, creatures and combats

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
|Play a combat | Implements a combat simulator to create and play combats against AI player

## Building & Running

### Dependencies
Only docker is required to get the full project up, you can run each services on his own, but you need to have a PostgreSQL and a RabbitMQ servers for full functionality.
In order to get a single service up you will need to have installed the JDK 17. 

### Getting up the project
In order to get all the project up you just need to run the `docker compose up` from the root of the project.
The port 80 should be free to get the api gateway listening for requests, besides the combats service needs to use the port 8081 for the combat simulation app.
When all the services are up you will have to run the migrations starting always by the players service ones, then the creatures service ones and finally the combats service migrations.
This is enough to get all the system ready for creating a combat betwen a created player `player one` and the created `ai` player which will perform its own movements.

### API documentation
The api has all the endpoints documented with swagger. You can access to that documentation on the url: `http://localhost:80/swagger` once the project is up with docker, check the api configuration for getting the port if you get the api up out of the docker compose file.
Swagger does not allow to perform GET requests with body, but you can us it any way and then copy the `curl` commnad generated paste it to a terminal and run it to get the results. There is not any issue with PUT and POST endpoints.

### Tests
There are unit tests implemented for the three services, players, creatures, combats, the e2e tests for the api gateway will be implemented soon. Also there are tests implemented for the match making and combat manager, testing a complete combat (the level increase tests are included on the creatures service unit tests).
You must indepently run the tests for each service.

## Services instructions
A separate README.md file has been included for each one of the services, you can find them on the root folder of each service. 



