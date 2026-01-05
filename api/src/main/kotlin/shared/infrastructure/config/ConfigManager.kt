package com.jatec.shared.infrastructure.config

import io.ktor.server.config.ApplicationConfig

class ConfigManager(config: ApplicationConfig) {
    val uriRabbitMQ = config.propertyOrNull("ktor.rabbitmq.uri")!!.getString()
    val sbExchange = config.propertyOrNull("ktor.rabbitmq.sb-exchange")!!.getString()

    val propertiesCorrelationId = config.propertyOrNull("ktor.rabbitmq.properties.correlation-id")!!.getString()
    val propertiesType = config.propertyOrNull("ktor.rabbitmq.properties.type")!!.getString()
    val headersKey = config.propertyOrNull("ktor.rabbitmq.properties.headers-key")!!.getString()
    val headersValue = config.propertyOrNull("ktor.rabbitmq.properties.headers-value")!!.getString()

    val playersRequestAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.players-request-all")!!.getString()
    val playersRequestAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.players-request-all")!!.getString()
    val playersResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.players-response-all")!!.getString()
    val playersResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.players-response-all")!!.getString()

    val playersRequestCreateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.players-request-create-one")!!.getString()
    val playersRequestCreateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.players-request-create-one")!!.getString()
    val playersResponseCreateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.players-response-create-one")!!.getString()
    val playersResponseCreateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.players-response-create-one")!!.getString()

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

    val playerCreaturesRequestUpdateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-request-update-one")!!.getString()
    val playerCreaturesRequestUpdateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-request-update-one")!!.getString()
    val playerCreaturesResponseUpdateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-response-update-one")!!.getString()
    val playerCreaturesResponseUpdateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-response-update-one")!!.getString()

    val combatsRequestAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.combats-request-all")!!.getString()
    val combatsRequestAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.combats-request-all")!!.getString()
    val combatsResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.combats-response-all")!!.getString()
    val combatsResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.combats-response-all")!!.getString()

    val combatsRequestCreateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.combats-request-create-one")!!.getString()
    val combatsRequestCreateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.combats-request-create-one")!!.getString()
    val combatsResponseCreateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.combats-response-create-one")!!.getString()
    val combatsResponseCreateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.combats-response-create-one")!!.getString()
    val playerCreaturesCombatResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-creatures-combat-response-all")!!.getString()
    val playerCreaturesCombatResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-creatures-combat-response-all")!!.getString()

    val playerCombatsRequestAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-combats-request-all")!!.getString()
    val playerCombatsRequestAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-combats-request-all")!!.getString()
    val playerCombatsResponseAllQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-combats-response-all")!!.getString()
    val playerCombatsResponseAllRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-combats-response-all")!!.getString()

    val playerCombatsRequestUpdateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-combats-request-update-one")!!.getString()
    val playerCombatsRequestUpdateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-combats-request-update-one")!!.getString()
    val playerCombatsResponseUpdateOneQueue = config.
    propertyOrNull("ktor.rabbitmq.queues.player-combats-response-update-one")!!.getString()
    val playerCombatsResponseUpdateOneRoutingKey = config.
    propertyOrNull("ktor.rabbitmq.routing-keys.player-combats-response-update-one")!!.getString()
}