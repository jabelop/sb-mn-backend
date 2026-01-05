package com.jatec.players.infrastructure.router

import com.jatec.players.application.builders.PlayerBuilder
import com.jatec.players.application.service.PlayersService
import com.jatec.players.application.dtos.MessageOptions
import com.jatec.players.application.dtos.MessagePlayerCreator
import com.jatec.players.infrastructure.controller.PlayersController
import com.jatec.players.application.exceptions.BadRequestException
import com.jatec.shared.application.dto.StandardResponse
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.infrastructure.communication.RabbitIdMessage
import com.jatec.shared.infrastructure.config.ConfigManager
import com.jatec.players.application.dtos.MessagePlayers
import com.jatec.players.application.exceptions.ClientExceptionChecker
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

fun Application.configurePlayerQueues(configManager: ConfigManager) {
    val playersService by inject<PlayersService>()
    val playersController = PlayersController(playersService = playersService)

    val queuesBindingFuture = CompletableFuture<Any?>()
    rabbitmq {
        queueBind {
            queue = configManager.playersRequestAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playersRequestAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playersRequestAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playersResponseAllQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playersResponseAllRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playersResponseAllQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playersRequestCreateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playersRequestCreateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playersRequestCreateOneQueue
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }

        queueBind {
            queue = configManager.playersResponseCreateOneQueue
            exchange = configManager.sbExchange
            routingKey = configManager.playersResponseCreateOneRoutingKey
            exchangeDeclare {
                exchange = configManager.sbExchange
                type = "direct"
            }
            queueDeclare {
                queue = configManager.playersResponseCreateOneQueue
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
                queue = configManager.playersRequestAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessageOptions>> { message ->
                    log.info("Received message: ${message.body}")
                    basicPublish {
                        log.info("Properties: $properties")
                        log.info("Correlation id: ${message.properties.correlationId}")
                        exchange = configManager.sbExchange
                        routingKey = configManager.playersResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        try {
                            val players = playersController.playersService.find(message.body.payload)
                            log.info("Sending players: ${players.size}")
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayers>(
                                    error = false,
                                    code = ResponseCodeManager.OK,
                                    msg = null,
                                    data = MessagePlayers(players = players))
                            )}
                        }
                        catch (e: BadRequestException) {
                            log.info("Sending bad request exception")
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayers>(
                                    error = true,
                                    code = ResponseCodeManager.CLIENT_ERROR,
                                    msg = e.message,
                                    data = null
                                )
                            )}
                        }
                        catch (e: Exception) {
                            log.info("Sending exception")
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload = StandardResponse<MessagePlayers>(
                                    error = true,
                                    code = ResponseCodeManager.SERVER_ERROR,
                                    msg = e.message,
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
                        routingKey = configManager.playersResponseAllRoutingKey
                        properties = basicProperties {
                            correlationId = configManager.propertiesCorrelationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }

                        message { RabbitIdMessage(
                            id = Json.decodeFromString<RabbitIdMessage<MessageOptions>>(message.body.toString()).id,
                            payload = StandardResponse<MessagePlayers>(
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
                queue = configManager.playersRequestCreateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<MessagePlayerCreator>> { message ->
                    log.info("Received message create one: ${message.body}")
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.playersResponseCreateOneRoutingKey
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
                            error = playersController.playersService.create(
                                PlayerBuilder.buildPlayerFromMessagePlayerCreator(message.body.payload)
                            )
                            messageResponse = "Created player: ${message.body.id} with name: ${message.body.payload.name}"
                            log.info(messageResponse)

                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload =  StandardResponse<String>(
                                    error = error,
                                    code = code,
                                    msg = messageResponse,
                                    data = null
                                )
                            )}
                        }
                        catch (e: Exception) {
                            if (ClientExceptionChecker.isClientException(e)){
                                messageResponse = "User with name ${message.body.payload.name} already exists"
                                code = ResponseCodeManager.CLIENT_ERROR
                                log.info("Player Exists error: $messageResponse")
                            } else {
                                messageResponse = "Internal server error"
                                code = ResponseCodeManager.SERVER_ERROR
                                log.info("Exception creating one: $messageResponse")
                            }
                            message { RabbitIdMessage(
                                id = message.body.id,
                                payload =  StandardResponse<String>(
                                    error = error,
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
                        routingKey = configManager.playersResponseCreateOneRoutingKey
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