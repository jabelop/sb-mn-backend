package com.jatec.combats.infrastructure.router

import com.jatec.combats.application.dto.MessageCombatCreatedData
import com.jatec.combats.application.dto.MessageCombats
import com.jatec.combats.application.dto.MessagePlayerCombatCreator
import com.jatec.combats.infrastructure.response.CombatsCreationResponseHandler
import com.jatec.combats.infrastructure.response.CombatsResponseHandlerManager
import com.jatec.combats.application.dto.MessagePlayerCombatCreatures
import com.jatec.combats.application.dto.MessageFinder
import com.jatec.combats.application.dto.MessagePlayerCombatData
import com.jatec.combats.application.dto.builders.MessageCombatCreatorBuilder
import com.jatec.combats.application.dto.builders.MessageFinderCreaturesBuilder
import com.jatec.players.application.dtos.MessagePlayers
import com.jatec.shared.application.dto.StandardResponse
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.infrastructure.communication.RabbitIdKeyMessage
import com.jatec.shared.infrastructure.communication.RabbitIdMessage
import com.jatec.shared.infrastructure.config.ConfigManager
import com.jatec.shared.infrastructure.response.ResponseHandler
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicConsume
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicProperties
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.basicPublish
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.exchangeDeclare
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.queueBind
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.queueDeclare
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.rabbitmq
import io.github.damir.denis.tudor.ktor.server.rabbitmq.rabbitMQ
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.UUID.randomUUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun Application.configureCombatsQueues(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    combatsResponseHandlerManager: CombatsResponseHandlerManager
) {
    val job = rabbitmq {
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
    }

    runBlocking {
        job.asCompletableFuture().await()
        configureCombatsAllRoute(
            configManager,
            responseCodeManager,
            combatsResponseHandlerManager
        )
        configurePlayerCombatsAllRoute(
            configManager,
            responseCodeManager,
            combatsResponseHandlerManager
        )
        configureCreateOneRoute(
            configManager,
            responseCodeManager,
            combatsResponseHandlerManager
        )
    }
}
fun Application.configureCombatsAllRoute(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    combatsResponseHandlerManager: CombatsResponseHandlerManager
) {
    routing {
        get("/combats") {
            val options = call.receive<MessageFinder>()
            val correlationId = randomUUID()

            val responseCombats = CompletableFuture<Any?>()
            val combatsResponseHandler = ResponseHandler(
                response = responseCombats,
                handler = call,
            )
            combatsResponseHandlerManager.combatsListResponseHandlerMap.put(
                correlationId.toString(),
                combatsResponseHandler
            )

            try {
                log.info("received query params ${call.request.rawQueryParameters}")
                val j = rabbitmq {
                    runBlocking {
                        log.info("received options $options")
                        basicPublish {
                            exchange = configManager.sbExchange
                            routingKey = configManager.combatsRequestAllRoutingKey
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
                        responseCombats.get(10, TimeUnit.SECONDS)
                        log.info("All done last combat")

                    }
                }
                j.start()
                j.asCompletableFuture().await()
            } catch (e: Exception) {
                log.info("Exception: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    StandardResponse<Any?>(
                        error = true,
                        code = ResponseCodeManager.SERVER_ERROR,
                        msg = e.message,
                        data = null
                    )
                )
            } finally {
                combatsResponseHandlerManager.combatsListResponseHandlerMap.remove(
                    correlationId.toString()
                )
            }
        }
        rabbitmq {
            basicConsume {
                autoAck = true
                queue = configManager.combatsResponseAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessageCombats>>> { message ->
                    log.info("message combats all: ${message.body}")
                    val responseHandler = combatsResponseHandlerManager.combatsListResponseHandlerMap.get(message.body.id)
                    responseHandler!!.handler.respond(
                        responseCodeManager.getApplicationErrorCode(message.body.payload.code!!),
                        message.body.payload
                    )
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
                    val messageId = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessageCombats>>>(message.body.decodeToString()).id
                    val responseHandler = combatsResponseHandlerManager.combatsListResponseHandlerMap.get(messageId)

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

fun Application.configurePlayerCombatsAllRoute(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    combatsResponseHandlerManager: CombatsResponseHandlerManager
) {
    routing {
        get("/player-combats-data") {
            val options = call.receive<MessageFinder>()
            val correlationId = randomUUID()

            val responsePlayerCombats = CompletableFuture<Any?>()
            val combatsResponseHandler = ResponseHandler(
                response = responsePlayerCombats,
                handler = call,
            )
            combatsResponseHandlerManager.combatsPlayerCombatDataResponseHandlerMap.put(
                correlationId.toString(),
                combatsResponseHandler
            )

            try {
                log.info("received query params ${call.request.rawQueryParameters}")
                val j = rabbitmq {
                    runBlocking {
                        log.info("received options $options")
                        basicPublish {
                            exchange = configManager.sbExchange
                            routingKey = configManager.playerCombatsRequestAllRoutingKey
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
                        responsePlayerCombats.get(10, TimeUnit.SECONDS)
                        log.info("All done last player combat")
                    }
                }
                j.start()
                j.asCompletableFuture().await()
            } catch (e: Exception) {
                log.info("Exception: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    StandardResponse<Any?>(
                        error = true,
                        code = ResponseCodeManager.SERVER_ERROR,
                        msg = e.message,
                        data = null
                    )
                )
            } finally {
                combatsResponseHandlerManager.combatsPlayerCombatDataResponseHandlerMap.remove(
                    correlationId.toString()
                )
            }
        }
        rabbitmq {
            basicConsume {
                autoAck = true
                queue = configManager.playerCombatsResponseAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessagePlayerCombatData>>> { message ->
                    log.info("message player combats all: ${message.body}")
                    val responseHandler = combatsResponseHandlerManager.combatsPlayerCombatDataResponseHandlerMap.get(message.body.id)
                    responseHandler!!.handler.respond(
                        responseCodeManager.getApplicationErrorCode(message.body.payload.code!!),
                        message.body.payload
                    )
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
                    val messageId = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessageCombats>>>(message.body.decodeToString()).id
                    val responseHandler = combatsResponseHandlerManager.combatsPlayerCombatDataResponseHandlerMap.get(messageId)

                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<Any>(
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
    combatsResponseHandlerManager: CombatsResponseHandlerManager
) {
    routing {
        rabbitmq {
            post("/combat") {
                val correlationId = randomUUID()
                val responseCreatures = CompletableFuture<Any?>()
                val playerCreaturesResponseHandler = CombatsCreationResponseHandler<MessagePlayerCombatCreatures>(
                    response = responseCreatures,
                    handler = call,
                    responseEnvelope = null
                )
                combatsResponseHandlerManager.combatsPlayerCreaturesResponseHandlerMap.put(
                    correlationId.toString(),
                   playerCreaturesResponseHandler
                )

                val responseCreateOne = CompletableFuture<Any?>()
                val responseOneResponseHandler = CombatsCreationResponseHandler<MessageCombats>(
                    response = responseCreateOne,
                    handler = call,
                    responseEnvelope = null
                )
                combatsResponseHandlerManager.combatsCreateOneResponseHandlerMap.put(
                    correlationId.toString(),
                    responseOneResponseHandler
                )
                try {
                    val messagePlayerCombatCreator = Json.decodeFromString<MessagePlayerCombatCreator>(call.receiveText())

                    // first get all the creatures for all players involved
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
                                routingKey = configManager.playerCreaturesCombatResponseAllRoutingKey,
                                payload = MessageFinderCreaturesBuilder
                                    .buildMessageFindCreaturesFromMessagePlayerCombatCreator(messagePlayerCombatCreator)
                            )
                        }
                    }

                    responseCreatures.get(10, TimeUnit.SECONDS)
                    if (playerCreaturesResponseHandler.responseEnvelope!!.error) {
                        playerCreaturesResponseHandler
                            .handler
                            .respond(playerCreaturesResponseHandler.responseEnvelope!!)
                        return@post
                    }

                    // make the request to create the combat
                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.combatsRequestCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        val combatDataOptions = playerCreaturesResponseHandler
                            .responseEnvelope!!
                            .data!!
                            .creatures

                        message {
                            RabbitIdMessage(
                                id = correlationId.toString(),
                                payload = MessageCombatCreatorBuilder
                                    .buildMessageCombatCreatorFromMessagePlayerCombatCreatorAndCombatData(
                                        messagePlayerCombatCreator,
                                        combatDataOptions
                                    )
                            )
                        }
                    }
                    log.info("sent combat for player 1 ${messagePlayerCombatCreator.player1Id} and player 2 ${messagePlayerCombatCreator.player2Id}")
                    responseCreateOne.get(10, TimeUnit.SECONDS)
                } catch (e: Exception) {
                    log.info("Exception: ${e.message}")
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessageCombats>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = e.message,
                            data = null
                        )
                    )
                } finally {
                    combatsResponseHandlerManager.combatsCreateOneResponseHandlerMap.remove(
                        correlationId.toString()
                    )

                    combatsResponseHandlerManager.combatsPlayerCreaturesResponseHandlerMap.remove(
                        correlationId.toString()
                    )
                }

            }
            // first listen to get all the creatures
            rabbitmq {
                basicConsume {
                    autoAck = true
                    queue = configManager.playerCreaturesCombatResponseAllQueue
                    dispatcher = Dispatchers.rabbitMQ
                    coroutinePollSize = 100

                    // If an exception is not properly handled in your business logic,
                    // it will be caught by the default Ktor coroutine scope.
                    // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                    deliverCallback<RabbitIdMessage<StandardResponse< MessagePlayerCombatCreatures>>> { message ->

                        log.info("Received message: ${message.body}")
                        log.info("Received id: ${message.body.payload.data?.creatures?.get(0)?.idPlayer}")
                        log.info("Received name: ${message.body.payload.data?.creatures?.get(0)?.name}")

                        val responseHandler = combatsResponseHandlerManager.combatsPlayerCreaturesResponseHandlerMap.get(message.body.id)
                        responseHandler?.responseEnvelope = message.body.payload
                        responseHandler?.response?.complete(null)
                    }

                    // Define a callback to handle deserialization failures.
                    // For example, you could redirect such messages to a dead-letter queue.
                    deliverFailureCallback { message ->
                        val errorMessage = "Received undeliverable message (deserialization failed): ${
                            message.body.decodeToString()
                        }"
                        log.info(errorMessage)

                        val messageId = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessagePlayerCombatCreatures>>>(message.body.decodeToString()).id
                        val responseHandler = combatsResponseHandlerManager.combatsCreateOneResponseHandlerMap.get(messageId)
                        val creaturesResponseHandler = combatsResponseHandlerManager.playerCombatsListResponseHandlerMap.get(messageId)

                        responseHandler!!.handler.respond(
                            HttpStatusCode.InternalServerError,
                            StandardResponse<MessagePlayers>(
                                error = true,
                                code = ResponseCodeManager.SERVER_ERROR,
                                msg = errorMessage,
                                data = null
                            )
                        )
                        responseHandler.response.complete(null)
                        creaturesResponseHandler!!.response.complete(null)
                    }
                }
            }

            // then do the rest
            basicConsume {
                autoAck = true
                queue = configManager.combatsResponseCreateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessageCombatCreatedData>>> { message ->

                    val responseHandler = combatsResponseHandlerManager.combatsCreateOneResponseHandlerMap.get(message.body.id)
                    responseHandler!!.handler.respond(
                        responseCodeManager.getApplicationErrorCode(message.body.payload.code!!),
                        message.body.payload
                    )
                    responseHandler.response.complete(null)
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->

                    val errorMessage = "Received undeliverable message (deserialization failed): ${
                        message.body.decodeToString()
                    }"
                    log.info(errorMessage)
                    val message = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessageCombatCreatedData>>>(message.body.decodeToString())
                    val responseHandler = combatsResponseHandlerManager.combatsListResponseHandlerMap.get(message.id)
                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayerCombatCreator>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = errorMessage,
                            data = null
                        )
                    )
                    responseHandler.response.complete(null)
                }

            }
        }
    }
}