
# mn backend design
Design document for the mn backend.

## Architecture
The application is implemented with the micro services architecture, having an api gateway and services for players, creatures and combats. The services are connected to the api through RabbitMQ queues. For simplicity just one data base is shared among all services, but for a production environment a single database for each micro service would be required in order to avoid a data base bottleneck.
Each micro service has been implemented following the hexagonal architecture dividing the service on the domain, application and infrastructure layers. Some related patterns like the repository one has been implemented for maintainability and testing purposes.

### Layers
The domain layer contains all related with the data, rules, and models representing it.
The application layer contains all the services, dtos and other code related with application specific purposes.
The infrastructure layer contains all the controllers, route handlers, specific technologies implementations and other code responsible for managing infrastructure specific details. 

### Application architecture overview
The application has the api gateway as a single entry point, only the combats service has the static entry point for serving the combat simulation and the WebSocket entry point to actually run the combat. This is designed this way to be able to run separate instances for different combats which handles scalability and gives more flexibility. 
The requests are handled in first instance by the api and the appropriate messages are sent to the their corresponding RabbitMQ queues which routes them to the appropriate services. The services are the actual workers for the response, performing validations, applying domain or application rules when necessary and performing the operations over the data. Because of the limitations found with the KTOR RabbitMQ plugin, the responses are returned over a different queue, having not found the way of get the "Direct reply to" mode working. As a future improvement probably working with the Kotlin specific library should avoid this avoiding at the same time the complexity of managing the response objects in memory.
In order to get the combat feature, a simple protocol over WebSockets has been implemented, making use of just four type of messages. Ports and adapters for the combat manager and combat match making has been implemented as well giving the ability to write new implementations if needed instead of modifying the existing ones.
For each service the repository pattern has been implemented, implementing ports on the domain layer and the adapters on the infrastructure one, giving the ability to write new implementations if needed without loosing the existing ones, due to a library change for example, and also the ability to write the unit tests implementing a test specific repository. For these purposes the Koin dependency injection library has been included in the project.


## Modeling

### Player
The player entity represents a player on the application, has been modeled with the attributes: 
- id: (uuid). 
- name: (string).

### Creature
The creature entity represents a creature that can be associated with a player, has been modeled with the attributes:
- id: (uuid).
- name: (string).  
- creatureClass: (string).
- level: (integer).
- xp: (integer).
- hp: (integer).
- speed: (integer).
- attack: (integer).

### Player creature
The player creatures entity represents a creature that has been associated with a player, has been modeled with the attributes:
- id: (uuid).
- name: (string).  
- idPlayer: (uuid).
- creatureClass: (uuid).
- level: (integer).
- xp: (integer).
- hp: (integer).
- speed: (integer).
- attack: (integer).

### Combat
The combat entity represents a created combat, between a player and the AI player only by now , has been modeled with the attributes:
- id: (uuid).
- idPlayer1: (uuid).  
- idPlayer2: (uuid).
- winner: (uuid).
- ip: (string).
- port: (integer).
- startedAt: (datetime).
- updatedAt: (datetime).
- finishedAt: (datetime).

### Combat Data
The combat entity represents creatures associated with a player in combat, has been modeled with the attributes:
- idCombat: (uuid)
- idPlayer: (uuid)
- id: (uuid).
- name: (string).  
- creatureClass (uuid).
- level: (integer).
- xp:(integer).
- hp: (integer).
- speed: (integer).
- attack: (integer).
- defense: (integer).
- timeToAttack: (integer).

### Turn management
The turn mangement is performed by the combat manager which uses the "timeToAttack" property assigned to every player creature assigned to a combat. Each second elapsed the "speed" property is subtracted from the previous property and the combat manager checks when that property is equal or less to cero, if anyone is found that creature will be the next to play and its "timeToAttack" will be restored to the default max value, which can be set from the properties.

### Combat design
The combat has been designed over simple rules:
- A player can not start a combat against itself.
- Right now the **player two must be always the created "ai" player**. Support for player vs player for future features.
- A combat between two players can not be created if  the players are in a combat not finished yet.
- The creatures only can perform two actions, attack or defense.
- Each time a creature has the turn can perform any of the actions and then await to its next turn.
- The creatures are not allowed to attack itselfs but they can give defenese to anyone and attack anyone else.    This decisions have been made in seek of flexibility.
- When a target creature receives an attack its health points (hp) will be decreased by the attack points of the source creature attacking, subtracting previously the target creature defense to the attack points.
- After a creature has recevied an attack its defense will be always cero.
- The combat ends when a player has all his creatures with no more health points, they are cero or less. Therefore the other is the winner.
- If a player not having the turn send a message this will be discarded.
- If a player send an invalid message having the turn, the server will respond notifing the error and the turn will advance to the next creature. 
- The combat can only in thre states: "PAUSED", "PLAYING", "FINISHED" while they are running. Not persisted storage for this by now.
The communication protocol is pretty straight forward two kind of messages are sent by the server: "WAIT" or "PLAY" and two kind of messages can are allowed to be sent by the player "CAUSED_DAMAGE" when a creature is attacking another one and "DEFENSE_RECEIVED" when a creature is giving defense to any of the creatures involved in the combat.

