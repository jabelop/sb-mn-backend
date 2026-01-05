package com.jatec.creatures.infrastructure.router

import com.jatec.creatures.application.service.CreaturesService
import com.jatec.creatures.application.dtos.MessageCreatureCreator
import com.jatec.creatures.application.dtos.MessageCreatures
import com.jatec.creatures.application.dtos.MessageOptions
import com.jatec.creatures.application.dtos.MessagePlayerCreatureUpdater
import com.jatec.creatures.application.dtos.MessagePlayerCreatures
import com.jatec.creatures.application.exceptions.ClientExceptionChecker
import com.jatec.creatures.infrastructure.controller.CreaturesController
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.application.dto.StandardResponse
import com.jatec.shared.infrastructure.communication.RabbitIdKeyMessage
import com.jatec.shared.infrastructure.communication.RabbitIdMessage
import com.jatec.shared.infrastructure.config.ConfigManager
import creatures.application.dtos.MessagePlayerCreatureUpdate
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicConsume
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicProperties
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicPublish
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.exchangeDeclare
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.queueBind
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.queueDeclare
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.rabbitmq
import io.github.damir.denis.tudor.ktor.server.rabbitmq.rabbitMQ
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json

import org.koin.ktor.ext.inject
import java.util.concurrent.CompletableFuture
import kotlin.getValue

fun Application.configureCreaturesQueues(configManager: ConfigManager) {
    val creaturesService by inject<CreaturesService>()
    val creaturesController = CreaturesController(creaturesService = creaturesService)
    val creaturesQueueBindingFuture = CompletableFuture<Any?>()
    rabbitmq {
        queueBind {
            queue = configManager.creaturesRequestAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.creaturesRequestAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.creaturesRequestAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.creaturesResponseAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.creaturesResponseAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.creaturesResponseAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.creaturesRequestCreateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.creaturesRequestCreateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.creaturesRequestCreateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.creaturesResponseCreateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.creaturesResponseCreateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.creaturesResponseCreateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCreaturesRequestAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCreaturesRequestAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCreaturesRequestAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCreaturesResponseAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCreaturesResponseAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCreaturesResponseAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCreaturesRequestUpdateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCreaturesRequestUpdateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCreaturesRequestUpdateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCreaturesResponseUpdateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCreaturesResponseUpdateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCreaturesResponseUpdateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCreaturesCombatResponseAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCreaturesCombatResponseAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCreaturesCombatResponseAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }
        creaturesQueueBindingFuture.complete(null)
    }
    creaturesQueueBindingFuture.get()

    routing {

        rabbitmq {
            basicConsume {
                autoAck = true
                queue = configManager.creaturesRequestAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessageOptions>> { message ->
                    log.info("Received message: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.creaturesResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        try {
                            val creatures = creaturesController.creaturesService.find(message.body.payload)
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessageCreatures>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessageCreatures(creatures = creatures))
                            )}
                        }
//                        catch (e: ClientException){
//
//                        }
                        catch (e: Exception) {
                            if (ClientExceptionChecker.isClientException(e)) {
                                message { RabbitIdMessage(
                                    id = message.body.id,
                                    payload = StandardResponse<MessageCreatures>(
                                        error = true,
                                        code = ResponseCodeManager.CLIENT_ERROR,
                                        msg = e.message,
                                        data = null
                                    )
                                )}
                            } else {
                                message { RabbitIdMessage(
                                    id = message.body.id,
                                    payload = StandardResponse<MessageCreatures>(
                                        error = true,
                                        code = ResponseCodeManager.SERVER_ERROR,
                                        msg = e.message,
                                        data = null
                                    )
                                )}
                            }
                        }
                    }

                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    val errorMessage = "Received undeliverable message (deserialization failed): ${
                        message.body.toString(
                            Charsets.UTF_8
                        )
                    }"
                    log.info(errorMessage)

                    basicPublish {
                        log.info("Properties: $properties")
                        log.info("Correlation id: ${message.properties.correlationId}")
                        exchange = configManager.sbExchange
                        routingKey = configManager.creaturesResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }

                        message { RabbitIdMessage(
                            id = Json.decodeFromString<RabbitIdMessage<MessageOptions>>(message.body.toString()).id,
                            payload = StandardResponse<String>(
                                error = true,
                                code = ResponseCodeManager.OK,
                                msg = errorMessage,
                                data = null
                            )
                        )}
                    }
                }
            }

            basicConsume {
                autoAck = true
                queue = configManager.creaturesRequestCreateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessageCreatureCreator>> { message ->
                    log.info("Received message create one creature: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.creaturesResponseCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        var messageResponse = ""
                        var error = true
                        var code = ResponseCodeManager.OK
                        try {
                            log.info("creating ${message.body}")
                            error = creaturesController.creaturesService.create(message.body.payload.options)
                            messageResponse = "Created the creature : ${message.body.payload.options.creatureId} for player: ${message.body.payload.options.playerId}"
                            log.info(messageResponse)
                        }
//                        catch(e: ClientException) {
//
//                        }
                        catch (e: Exception) {
                            if (ClientExceptionChecker.isClientException(e)) {
                                log.info("Client exception: ${e.message}")
                                messageResponse = e.message!!
                                code = ResponseCodeManager.CLIENT_ERROR
                            } else {
                                log.info("Exception: ${e.message}")
                                messageResponse = "Internal server error"
                                code = ResponseCodeManager.SERVER_ERROR
                            }
                        }
                        message { RabbitIdMessage(
                            id = message.body.id,
                            payload = StandardResponse<String?>(
                                error = error,
                                code = code,
                                msg = messageResponse,
                                data = null
                            )
                        )}
                    }
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    val errorMessage = "Received undeliverable message (deserialization failed): ${
                        message.body.toString(
                            Charsets.UTF_8
                        )
                    }"
                    log.info(errorMessage)

                    basicPublish {
                        log.info("Properties: $properties")
                        log.info("Correlation id: ${message.properties.correlationId}")
                        exchange = configManager.sbExchange
                        routingKey = configManager.creaturesResponseCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }

                        message { RabbitIdMessage(
                            id = Json.decodeFromString<RabbitIdMessage<MessageOptions>>(message.body.toString()).id,
                            payload = StandardResponse<String>(
                                error = true,
                                code = ResponseCodeManager.OK,
                                msg = errorMessage,
                                data = null
                            )
                        )}
                    }
                }
            }

            basicConsume {
                autoAck = true
                queue = configManager.playerCreaturesRequestAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback< RabbitIdKeyMessage<MessageOptions>> { message ->
                    log.info("Received message: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = message.body.routingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        try {
                            val creatures = creaturesController.creaturesService.findPlayerCreatures(message.body.payload)
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayerCreatures>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessagePlayerCreatures(creatures = creatures)
                                )
                            )}
                            log.info("Sending Results")
                        }
//                        catch (e: ClientException) {
//
//                        }
                        catch (e: Exception) {
                            if (ClientExceptionChecker.isClientException(e)) {
                                log.info("Sending Client exception: ${e.message}")
                                message { RabbitIdMessage(
                                    id = message.body.id,
                                    payload = StandardResponse<MessagePlayerCreatures>(
                                        error = true,
                                        code = ResponseCodeManager.CLIENT_ERROR,
                                        msg = e.message,
                                        data = null
                                    )
                                )}
                            } else {
                                log.info("Sending exception")
                                message {
                                    RabbitIdMessage(
                                        id = message.body.id,
                                        payload = StandardResponse<MessagePlayerCreatures>(
                                            error = true,
                                            code = ResponseCodeManager.SERVER_ERROR,
                                            msg = e.message,
                                            data = null
                                        )
                                    )
                                }
                            }
                        }
                    }

                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    val errorMessage = "Received undeliverable message (deserialization failed): ${
                        message.body.toString(
                            Charsets.UTF_8
                        )
                    }"
                    log.info(errorMessage)

                    basicPublish {
                        log.info("Properties: $properties")
                        log.info("Correlation id: ${message.properties.correlationId}")
                        exchange = configManager.sbExchange
                        routingKey = configManager.playerCreaturesResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }

                        message { RabbitIdMessage(
                            id = Json.decodeFromString<RabbitIdMessage<MessageOptions>>(message.body.toString()).id,
                            payload = StandardResponse<String>(
                                error = true,
                                code = ResponseCodeManager.OK,
                                msg = errorMessage,
                                data = null
                            )
                        )}
                    }
                }
            }

            basicConsume {
                autoAck = true
                queue = configManager.playerCreaturesRequestUpdateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessagePlayerCreatureUpdater>> { message ->
                    log.info("Received message update one creature: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.playerCreaturesResponseUpdateOneRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        var messageResponse = ""
                        var code = ResponseCodeManager.OK
                        var data: Any? = null
                        try {
                            log.info("updating: ${message.body}")
                            val playerCreature = creaturesController.creaturesService.updatePlayerCreature(message.body.payload.options)
                            messageResponse = "Updated the creature : ${message.body.payload.options.creatureId}"
                            log.info(messageResponse)
                            data = playerCreature

                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayerCreatureUpdate>(
                                    error = false,
                                    code = code,
                                    msg = messageResponse,
                                    data = MessagePlayerCreatureUpdate(
                                        creature = data
                                    )
                                )
                            )}
                        }
//                        catch(e: ClientException) {
//
//                        }
                        catch (e: Exception) {
                            if (ClientExceptionChecker.isClientException(e)) {
                                log.info("Client exception: ${e.message}")
                                messageResponse = e.message!!
                                code = ResponseCodeManager.CLIENT_ERROR
                            } else {
                                log.info("Exception: ${e.message}")
                                messageResponse = "Internal server error"
                                code = ResponseCodeManager.SERVER_ERROR
                            }

                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayerCreatureUpdate>(
                                    error = true,
                                    code = code,
                                    msg = messageResponse,
                                    data = MessagePlayerCreatureUpdate(
                                        creature = null
                                    )
                                )
                            )}
                        }

                    }
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    val errorMessage = "Received undeliverable message (deserialization failed): ${
                        message.body.toString(
                            Charsets.UTF_8
                        )
                    }"
                    log.info(errorMessage)

                    basicPublish {
                        log.info("Properties: $properties")
                        log.info("Correlation id: ${message.properties.correlationId}")
                        exchange = configManager.sbExchange
                        routingKey = configManager.creaturesResponseCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }

                        message { RabbitIdMessage(
                            id = Json.decodeFromString<RabbitIdMessage<MessageOptions>>(message.body.toString()).id,
                            payload = StandardResponse<String>(
                                error = true,
                                code = ResponseCodeManager.OK,
                                msg = errorMessage,
                                data = null
                            )
                        )}
                    }
                }
            }
        }
    }
}