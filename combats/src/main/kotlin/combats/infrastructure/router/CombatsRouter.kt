package com.jatec.combats.infrastructure.router

import com.jatec.combats.application.service.CombatsService
import com.jatec.combats.application.dtos.MessageCombatCreatedData
import com.jatec.combats.application.dtos.MessageCombatCreator
import com.jatec.combats.application.dtos.MessageCombats
import com.jatec.combats.application.dtos.MessageOptions
import com.jatec.combats.application.dtos.MessagePlayerCombatUpdater
import com.jatec.combats.application.dtos.MessagePlayerCombats
import com.jatec.combats.application.exceptions.ClientExceptionChecker
import com.jatec.combats.infrastructure.controller.CombatsController
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.application.dto.StandardResponse
import com.jatec.shared.infrastructure.communication.RabbitIdMessage
import com.jatec.shared.infrastructure.config.ConfigManager
import combats.application.dtos.MessagePlayerCombatUpdate
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



fun Application.configureCombatsQueues(configManager: ConfigManager) {
    val combatsService by inject<CombatsService>()
    val combatsController = CombatsController(combatsService = combatsService)
    val queuesBindingFuture = CompletableFuture<Any?>()
    rabbitmq {
        queueBind {
            queue = configManager.combatsRequestAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.combatsRequestAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.combatsRequestAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.combatsResponseAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.combatsResponseAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.combatsResponseAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.combatsRequestCreateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.combatsRequestCreateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.combatsRequestCreateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.combatsResponseCreateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.combatsResponseCreateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.combatsResponseCreateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCombatsRequestAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCombatsRequestAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCombatsRequestAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCombatsResponseAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCombatsResponseAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCombatsResponseAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCombatsRequestUpdateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCombatsRequestUpdateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCombatsRequestUpdateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playerCombatsResponseUpdateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playerCombatsResponseUpdateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playerCombatsResponseUpdateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }
        queuesBindingFuture.complete(null)
    }

    queuesBindingFuture.get()

    routing {

        rabbitmq {
            basicConsume {
                autoAck = true
                queue = configManager.combatsRequestAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessageOptions>> { message ->
                    log.info("Received message: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.combatsResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        try {
                            val combats = combatsController.combatsService.find(message.body.payload.where)
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessageCombats>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessageCombats(combats = combats))
                            )}
                        }
                        catch (e: Exception) {
                            if (ClientExceptionChecker.isClientException(e)) {
                                message { RabbitIdMessage(
                                    id = message.body.id,
                                    payload = StandardResponse<MessageCombats>(
                                        error = true,
                                        code = ResponseCodeManager.CLIENT_ERROR,
                                        msg = e.message,
                                        data = null
                                    )
                                )}
                            } else {
                                message {
                                    RabbitIdMessage(
                                        id = message.body.id,
                                        payload = StandardResponse<MessageCombats>(
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
                        routingKey = configManager.combatsResponseAllRoutingKey
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
                queue = configManager.combatsRequestCreateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessageCombatCreator>> { message ->
                    log.info("Received message create one combat: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.combatsResponseCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        var messageResponse = ""
                        var code = ResponseCodeManager.OK
                        try {
                            log.info("creating ${message.body}")
                           val response = combatsController.combatsService.create(message.body.payload.options)
                            messageResponse = "Created the combat p1: ${message.body.payload.options.player1Id} -> p2: ${message.body.payload.options.player2Id}"
                            log.info(messageResponse)
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessageCombatCreatedData>(
                                    error = false,
                                    code = code,
                                    msg = messageResponse,
                                    data = response
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
                                payload = StandardResponse<MessageCombatCreatedData>(
                                    error = true,
                                    code = code,
                                    msg = messageResponse,
                                    data = null
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
                        routingKey = configManager.combatsResponseCreateOneRoutingKey
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
                queue = configManager.playerCombatsRequestAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessageOptions>> { message ->
                    log.info("Received message: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.playerCombatsResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        try {
                            val combatData = combatsController.combatsService.findPlayerCombats(message.body.payload.where)
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayerCombats>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessagePlayerCombats(combatData = combatData)
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
                                    payload = StandardResponse<MessagePlayerCombats>(
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
                                        payload = StandardResponse<MessagePlayerCombats>(
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
                        routingKey = configManager.playerCombatsResponseAllRoutingKey
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
                queue = configManager.playerCombatsRequestUpdateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessagePlayerCombatUpdater>> { message ->
                    log.info("Received message update one combat: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.playerCombatsResponseUpdateOneRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        var messageResponse = ""
                        var code = ResponseCodeManager.OK
                        try {
                            log.info("updating: ${message.body}")
                            val data = combatsController.combatsService.updatePlayerCombat(message.body.payload.options)
                            messageResponse = "Updated the combat : ${message.body.payload.options.combatId}"
                            log.info(messageResponse)

                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayerCombatUpdate>(
                                    error = false,
                                    code = code,
                                    msg = messageResponse,
                                    data = MessagePlayerCombatUpdate(
                                        combat = data
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
                                payload = StandardResponse<MessagePlayerCombatUpdate>(
                                    error = true,
                                    code = code,
                                    msg = messageResponse,
                                    data = MessagePlayerCombatUpdate(
                                        combat = null
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
                        routingKey = configManager.combatsResponseCreateOneRoutingKey
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