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

}