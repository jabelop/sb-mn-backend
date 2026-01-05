package com.jatec.players.infrastructure.router

import com.jatec.players.application.builders.MessageFinderCreaturesBuilder
import com.jatec.players.application.dtos.MessageFinder
import com.jatec.players.application.dtos.MessagePlayerCreation
import com.jatec.players.application.dtos.MessagePlayerCreator
import com.jatec.players.application.dtos.MessagePlayerCreatorRequest
import com.jatec.players.application.dtos.MessagePlayerCreatures
import com.jatec.players.application.dtos.MessagePlayers
import com.jatec.players.application.dtos.MessagePlayersCreatures
import com.jatec.players.infrastructure.response.PlayersResponseHandlerManager
import com.jatec.players.infrastructure.response.PlayersResponseHandler
import com.jatec.shared.application.dto.StandardResponse
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.infrastructure.communication.RabbitIdKeyMessage
import com.jatec.shared.infrastructure.communication.RabbitIdMessage
import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.util.UUID.randomUUID
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
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun Application.configurePlayerQueues(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    playersResponseHandlerManager: PlayersResponseHandlerManager) {
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
        queuesBindingFuture.complete(null)
    }
    queuesBindingFuture.get()
    configurePlayerAllRouteObservable(configManager, responseCodeManager, playersResponseHandlerManager)
    configureCreateOneRoute(configManager, responseCodeManager, playersResponseHandlerManager)
}

fun Application.configurePlayerAllRouteObservable(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    playersResponseHandlerManager: PlayersResponseHandlerManager) {

    routing {
        get("/players") {
            log.info("Requesting players")
            val options = call.receive<MessageFinder>()
            log.info("$options")
            val correlationId = randomUUID()

            val responsePlayers = CompletableFuture<Any?>()
            val playersResponseHandler = PlayersResponseHandler<MessagePlayers>(
                response = responsePlayers,
                handler = call,
                responseEnvelope = null
            )
            playersResponseHandlerManager.playersListResponseHandlerMap.put(
                correlationId.toString(),
                playersResponseHandler
            )
            val responseCreatures = CompletableFuture<Any?>()
            val playerCreaturesResponseHandler = PlayersResponseHandler<MessagePlayerCreatures>(
                response = responseCreatures,
                handler = call,
                responseEnvelope = null
            )
            playersResponseHandlerManager.playerCreaturesListResponseHandlerMap.put(
                correlationId.toString(),
                playerCreaturesResponseHandler
            )

            try {
                log.info("received query params ${call.request.rawQueryParameters}")
                val j = rabbitmq {
                    runBlocking {
                        log.info("received options $options")
                        basicPublish {
                            exchange = configManager.sbExchange
                            routingKey = configManager.playersRequestAllRoutingKey
                            properties = basicProperties {
                                correlationId
                                type = configManager.propertiesType
                                headers =
                                    mapOf(configManager.headersKey to configManager.headersValue)
                            }
                            message {
                                RabbitIdMessage(
                                    id = correlationId.toString(),
                                    payload = options
                                )
                            }
                        }
                        log.info("message Sent")
                        basicPublish {
                            exchange = configManager.sbExchange
                            routingKey = configManager.playerCreaturesRequestAllRoutingKey
                            properties = basicProperties {
                                correlationId
                                type = configManager.propertiesType
                                headers =
                                    mapOf(configManager.headersKey to configManager.headersValue)
                            }
                            message {
                                RabbitIdKeyMessage(
                                    id = correlationId.toString(),
                                    routingKey = configManager.playerCreaturesResponseAllRoutingKey,
                                    payload = MessageFinderCreaturesBuilder.buildMessageFinderCreaturesFromMessageFinderPlayer(
                                        options
                                    )
                                )
                            }
                        }
                        val allCompletables = CompletableFuture.allOf(
                            responsePlayers,
                            responseCreatures
                        )
                        allCompletables.get(10, TimeUnit.SECONDS)
                        log.info("All done last player creatures")
                        val players = playersResponseHandler.responseEnvelope?.data?.players
                        val clientError =
                            (playersResponseHandler.responseEnvelope?.error !== null && playersResponseHandler.responseEnvelope!!.error) ||
                                    (playerCreaturesResponseHandler.responseEnvelope?.error !== null && playerCreaturesResponseHandler.responseEnvelope!!.error)

                        val clientCode =
                            if (playersResponseHandler.responseEnvelope?.code!! > playerCreaturesResponseHandler.responseEnvelope!!.code!!) {
                                playersResponseHandler.responseEnvelope!!.code
                            } else playerCreaturesResponseHandler.responseEnvelope!!.code
                        var clientMsg: String? = playersResponseHandler.responseEnvelope?.msg
                        clientMsg =
                            if (playerCreaturesResponseHandler.responseEnvelope?.msg == null) clientMsg else "$clientMsg . ${playerCreaturesResponseHandler.responseEnvelope!!.msg}"
                        val creatures =
                            playerCreaturesResponseHandler.responseEnvelope?.data?.creatures
                        call.respond(
                            responseCodeManager.getApplicationErrorCode(clientCode!!),
                            StandardResponse<MessagePlayersCreatures>(
                                error = clientError,
                                code = clientCode,
                                msg = clientMsg,
                                data = MessagePlayersCreatures(
                                    players = players,
                                    creatures = creatures
                                )
                            )
                        )
                    }
                }
                j.start()
                j.asCompletableFuture().await()
            } catch (e: Exception) {
                log.info("Exception: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    StandardResponse<MessagePlayers>(
                        error = true,
                        code = ResponseCodeManager.SERVER_ERROR,
                        msg = e.message,
                        data = null
                    )
                )
            } finally {
                playersResponseHandlerManager.playersListResponseHandlerMap.remove(
                    correlationId.toString()
                )
                playersResponseHandlerManager.playerCreaturesListResponseHandlerMap.remove(
                    correlationId.toString()
                )
            }
        }
        rabbitmq {
            basicConsume {
                autoAck = true
                queue = configManager.playersResponseAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessagePlayers>>> { message ->
                    log.info("message players all: ${message.body}")
                    val responseHandler = playersResponseHandlerManager.playersListResponseHandlerMap.get(message.body.id)
                    responseHandler!!.responseEnvelope = message.body.payload
                    responseHandler.response.complete(null)
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    log.info(
                        "Received undeliverable message (deserialization failed): ${
                            message.body.decodeToString()
                        }"
                    )
                    val messageId = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessagePlayers>>>(message.body.decodeToString()).id
                    val responseHandler = playersResponseHandlerManager.playersListResponseHandlerMap.get(messageId)
                    val creaturesResponseHandler = playersResponseHandlerManager.playerCreaturesListResponseHandlerMap.get(messageId)

                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayers>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = message.body.decodeToString(),
                            data = null
                        )
                    )
                    responseHandler.response.complete(null)
                    creaturesResponseHandler!!.response.complete(null)
                }
            }

            basicConsume {
                autoAck = true
                queue = configManager.playerCreaturesResponseAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessagePlayerCreatures>>> { message ->

                    log.info("message Player creatures: ${message.body}")
                    val responseHandler = playersResponseHandlerManager.playerCreaturesListResponseHandlerMap.get(message.body.id)
                    responseHandler!!.responseEnvelope = message.body.payload
                    responseHandler.response.complete(null)
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    log.info(
                        "Received undeliverable message (deserialization failed): ${
                            message.body.decodeToString()
                        }"
                    )
                    val messageId = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessagePlayersCreatures>>>(message.body.decodeToString()).id
                    val responseHandler = playersResponseHandlerManager.playerCreaturesListResponseHandlerMap.get(messageId)

                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayers>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = message.body.decodeToString(),
                            data = null
                        )
                    )
                    responseHandler.response.complete(null)
                }
            }
        }

    }
}

fun Application.configureCreateOneRoute(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    playersResponseHandlerManager: PlayersResponseHandlerManager
) {
    routing {
        var name = ""
        rabbitmq {
            post("/player") {
                val playerName = call.receive<MessagePlayerCreatorRequest>()
                val correlationId = randomUUID()
                val response = CompletableFuture<Any?>()
                playersResponseHandlerManager.playersCreateOneResponseHandlerMap.put(
                    correlationId.toString(),
                    PlayersResponseHandler(response, handler = call, responseEnvelope = null)
                )

                try {
                    log.info("received player ${call.request.queryParameters}")
                    name = playerName.name
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.playersRequestCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        message {
                            RabbitIdMessage(
                                id = correlationId.toString(),
                                payload = MessagePlayerCreator(
                                    id = correlationId.toString(),
                                    name = playerName.name
                                )
                            )
                        }
                    }
                    log.info("sent player ${playerName.name}")
                    response.get(10, TimeUnit.SECONDS)
                }  catch (e: Exception) {
                    log.info("Exception: ${e.message}")
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayerCreator>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = e.message,
                            data = null
                        )
                    )
                } finally {
                    playersResponseHandlerManager.playersCreateOneResponseHandlerMap.remove(
                        correlationId.toString()
                    )
                }

            }
            basicConsume {
                autoAck = true
                queue = configManager.playersResponseCreateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessagePlayerCreation>>> { message ->
                    if (!message.body.payload.error) log.info("Player created persisted with id: ${message.body.id}")
                    log.info("message: ${message.body}")
                    val responseHandler = playersResponseHandlerManager.playersCreateOneResponseHandlerMap.get(message.body.id)
                    responseHandler!!.handler.respond(
                        responseCodeManager.getApplicationErrorCode(message.body.payload.code!!),
                        message.body.payload
                    )
                    responseHandler.response.complete(null)
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    val messageId = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessagePlayerCreator>>>(message.body.decodeToString()).id
                    val responseHandler = playersResponseHandlerManager.playersCreateOneResponseHandlerMap.get(messageId)
                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayerCreation>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = message.body.decodeToString(),
                            data = null
                        )
                    )
                    responseHandler.response.complete(null)
                }
            }
        }
    }
}
