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