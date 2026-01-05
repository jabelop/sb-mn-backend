package com.jatec.shared.infrastructure.config

import io.ktor.server.config.ApplicationConfig

class ConfigManager(config: ApplicationConfig) {

    val urlBD = config.propertyOrNull("ktor.database.url")!!.getString()
    val userDB = config.propertyOrNull("ktor.database.user")!!.getString()
    val passwordDB = config.propertyOrNull("ktor.database.password")!!.getString()

    val uriRabbitMQ = config.propertyOrNull("ktor.rabbitmq.uri")!!.getString()
    val sbExchange = config.propertyOrNull("ktor.rabbitmq.sb-exchange")!!.getString()

    val propertiesCorrelationId = config.propertyOrNull("ktor.rabbitmq.properties.correlation-id")!!.getString()
    val propertiesType = config.propertyOrNull("ktor.rabbitmq.properties.type")!!.getString()
    val headersKey = config.propertyOrNull("ktor.rabbitmq.properties.headers-key")!!.getString()
    val headersValue = config.propertyOrNull("ktor.rabbitmq.properties.headers-value")!!.getString()

    val creaturesRequestAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.creatures-request-all")!!.getString()
    val creaturesRequestAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.creatures-request-all")!!.getString()
    val creaturesResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.creatures-response-all")!!.getString()
    val creaturesResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.creatures-response-all")!!.getString()

    val creaturesRequestCreateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.creatures-request-create-one")!!.getString()
    val creaturesRequestCreateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.creatures-request-create-one")!!.getString()
    val creaturesResponseCreateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.creatures-response-create-one")!!.getString()
    val creaturesResponseCreateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.creatures-response-create-one")!!.getString()

    val playerCreaturesRequestAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-request-all")!!.getString()
    val playerCreaturesRequestAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-request-all")!!.getString()
    val playerCreaturesResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-response-all")!!.getString()
    val playerCreaturesResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-response-all")!!.getString()
    val playerCreaturesCombatResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-combat-response-all")!!.getString()
    val playerCreaturesCombatResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-combat-response-all")!!.getString()

    val playerCreaturesRequestUpdateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-request-update-one")!!.getString()
    val playerCreaturesRequestUpdateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-request-update-one")!!.getString()
    val playerCreaturesResponseUpdateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-response-update-one")!!.getString()
    val playerCreaturesResponseUpdateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-response-update-one")!!.getString()

}