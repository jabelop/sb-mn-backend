package com.jatec.creatures.infrastructure.router

import com.jatec.creatures.application.dtos.MessageCreatureCreator
import com.jatec.creatures.application.dtos.MessageCreatures
import com.jatec.shared.application.dto.creatures.MessageFinder
import com.jatec.creatures.application.dtos.MessagePlayerCreatureCreation
import com.jatec.creatures.application.dtos.MessagePlayerCreatureCreator
import com.jatec.creatures.application.dtos.MessagePlayerCreatureUpdate
import com.jatec.creatures.application.dtos.MessagePlayerCreatureUpdater
import com.jatec.shared.infrastructure.response.ResponseHandler
import com.jatec.creatures.infrastructure.response.CreaturesResponseHandlerManager
import com.jatec.shared.application.dto.StandardResponse
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.infrastructure.communication.RabbitIdMessage
import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
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
import io.ktor.server.routing.put
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.util.UUID.randomUUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun Application.configureCreaturesQueues(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    creaturesResponseHandlerManager: CreaturesResponseHandlerManager
    ) {
    val queuesBindingFuture = CompletableFuture<Any?>()
    val bindingJob = rabbitmq {
        runBlocking {

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
            queuesBindingFuture.complete(null)
        }
    }
    runBlocking {
        bindingJob.start()
        bindingJob.asCompletableFuture().await()
        queuesBindingFuture.get()
        configureCreaturesAllRoute(
            configManager,
            responseCodeManager,
            creaturesResponseHandlerManager
        )
        configureCreateOneRoute(configManager, responseCodeManager, creaturesResponseHandlerManager)
        configureUpdateOnePlayerCreatureRoute(
            configManager,
            responseCodeManager,
            creaturesResponseHandlerManager
        )
    }
}

fun Application.configureCreaturesAllRoute(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    creaturesResponseHandlerManager: CreaturesResponseHandlerManager
    ) {
    routing {
        get("/creatures") {

            val options = call.receive<MessageFinder>()
            val correlationId = randomUUID()
            val response = CompletableFuture<Any?>()
            creaturesResponseHandlerManager.creaturesListResponseHandlerMap.put(
                correlationId.toString(),
                ResponseHandler(response, handler = call)
            )
            try {
                log.info("received query params ${call.request.rawQueryParameters}")
                val j = rabbitmq {
                    runBlocking {
                        log.info("received options $options")
                        basicPublish {
                            exchange = configManager.sbExchange
                            routingKey = configManager.creaturesRequestAllRoutingKey
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
                        response.get(10, TimeUnit.SECONDS)
                    }
                }
                j.start()
                j.asCompletableFuture().await()
            } catch (e: Exception) {
                log.info("Exception: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    StandardResponse<Any>(
                        error = true,
                        code = ResponseCodeManager.SERVER_ERROR,
                        msg = e.message,
                        data = null
                    )
                )
            } finally {
                creaturesResponseHandlerManager.creaturesListResponseHandlerMap.remove(
                    correlationId.toString()
                )
            }
        }
        rabbitmq {
            basicConsume {
                autoAck = true
                queue = configManager.creaturesResponseAllQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessageCreatures>>> { message ->

                    log.info("Received message: ${message.body}")
                    log.info("Received id: ${message.body.payload.data?.creatures?.get(0)?.id}")
                    log.info("Received name: ${message.body.payload.data?.creatures?.get(0)?.name}")

                    val responseHandler = creaturesResponseHandlerManager.creaturesListResponseHandlerMap.get(message.body.id)
                    responseHandler?.handler?.respond(
                        responseCodeManager.getApplicationErrorCode(message.body.payload.code!!),
                        message.body.payload
                    )
                    creaturesResponseHandlerManager.creaturesListResponseHandlerMap.remove(message.body.id)
                    responseHandler?.response?.complete(null)
                }

                // Define a callback to handle deserialization failures.
                // For example, you could redirect such messages to a dead-letter queue.
                deliverFailureCallback { message ->
                    val errorMessage = "Received undeliverable message (deserialization failed): ${
                        message.body.decodeToString()
                    }"
                    log.info(errorMessage)

                    val message = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessageCreatures>>>(message.body.decodeToString())
                    val responseHandler = creaturesResponseHandlerManager.creaturesListResponseHandlerMap.get(message.id)
                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessageFinder>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = errorMessage,
                            data = null
                        )
                    )
                    creaturesResponseHandlerManager.creaturesListResponseHandlerMap.remove(message.id)
                    responseHandler.response.complete(null)
                }
            }
        }

    }
}

fun Application.configureCreateOneRoute(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    creaturesResponseHandlerManager: CreaturesResponseHandlerManager
    ) {
    routing {
        rabbitmq {
            post("/creature") {
                val correlationId = randomUUID()
                val response = CompletableFuture<Any?>()
                creaturesResponseHandlerManager.creaturesCreateOneResponseHandlerMap.put(
                    correlationId.toString(),
                    ResponseHandler(response, handler = call)
                )
                try {
                    log.info("received player creature ${call.request.queryParameters}")
                    val messagePlayerCreatureCreator = call.receive<MessagePlayerCreatureCreator>()
                    val playerId = messagePlayerCreatureCreator.playerId
                    val creatureId = messagePlayerCreatureCreator.creatureId

                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.creaturesRequestCreateOneRoutingKey
                        properties = basicProperties {
                            correlationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        message {
                            RabbitIdMessage(
                                id = correlationId.toString(),
                                payload = MessageCreatureCreator(
                                   options = MessagePlayerCreatureCreator(
                                       playerId = playerId,
                                       creatureId = creatureId
                                   )
                                )
                            )
                        }
                    }
                    log.info("sent creature $creatureId for player $playerId")
                    response.get(10, TimeUnit.SECONDS)
                } catch (e: Exception) {
                    log.info("Exception: ${e.message}")
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessageCreatures>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = e.message,
                            data = null
                        )
                    )
                } finally {
                    creaturesResponseHandlerManager.creaturesCreateOneResponseHandlerMap.remove(
                        correlationId.toString()
                    )
                }

            }
            basicConsume {
                autoAck = true
                queue = configManager.creaturesResponseCreateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessagePlayerCreatureCreation>>> { message ->

                    val responseHandler = creaturesResponseHandlerManager.creaturesCreateOneResponseHandlerMap.get(message.body.id)
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

                    val message = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessageCreatures>>>(message.body.decodeToString())
                    val responseHandler = creaturesResponseHandlerManager.creaturesListResponseHandlerMap.get(message.id)
                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayerCreatureCreator>(
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

fun Application.configureUpdateOnePlayerCreatureRoute(
    configManager: ConfigManager,
    responseCodeManager: ResponseCodeManager<HttpStatusCode>,
    creaturesResponseHandlerManager: CreaturesResponseHandlerManager
) {
    routing {
        rabbitmq {
            put("/creature") {
                val correlationId = randomUUID()
                val response = CompletableFuture<Any?>()
                creaturesResponseHandlerManager.creaturesUpdateOneResponseHandlerMap.put(
                    correlationId.toString(),
                    ResponseHandler(response, handler = call)
                )
                try {
                    log.info("received player creature to update ${call.request.queryParameters}")
                    val messagePlayerCreatureCreator = call.receive<MessagePlayerCreatureUpdater>()
                    val creatureId = messagePlayerCreatureCreator.options.creatureId
                    val xp = messagePlayerCreatureCreator.options.xp

                    basicPublish {
                        exchange = configManager.sbExchange
                        routingKey = configManager.playerCreaturesRequestUpdateOneRoutingKey
                        properties = basicProperties {
                            correlationId
                            type = configManager.propertiesType
                            headers = mapOf(configManager.headersKey to configManager.headersValue)
                        }
                        message {
                            RabbitIdMessage(
                                id = correlationId.toString(),
                                payload = MessagePlayerCreatureUpdater(options = messagePlayerCreatureCreator.options)
                            )
                        }
                    }
                    log.info("sent update creature $creatureId to increase xp: $xp")
                    response.get(10, TimeUnit.SECONDS)
                } catch (e: Exception) {
                    log.info("Exception: ${e.message}")
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessageCreatures>(
                            error = true,
                            code = ResponseCodeManager.SERVER_ERROR,
                            msg = e.message,
                            data = null
                        )
                    )
                } finally {
                    creaturesResponseHandlerManager.creaturesUpdateOneResponseHandlerMap.remove(
                        correlationId.toString()
                    )
                }

            }
            basicConsume {
                autoAck = true
                queue = configManager.playerCreaturesResponseUpdateOneQueue
                dispatcher = Dispatchers.rabbitMQ
                coroutinePollSize = 100

                // If an exception is not properly handled in your business logic,
                // it will be caught by the default Ktor coroutine scope.
                // By defining your own coroutine scope, you gain more flexibility in handling exceptions.
                deliverCallback<RabbitIdMessage<StandardResponse<MessagePlayerCreatureUpdate>>> { message ->
                    val responseHandler = creaturesResponseHandlerManager.creaturesUpdateOneResponseHandlerMap.get(message.body.id)
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

                    val bodyString = message.body.decodeToString()
                    val message = Json.decodeFromString<RabbitIdMessage<StandardResponse<MessagePlayerCreatureUpdate>>>(bodyString)
                    val responseHandler = creaturesResponseHandlerManager.creaturesUpdateOneResponseHandlerMap.get(message.id)
                    responseHandler!!.handler.respond(
                        HttpStatusCode.InternalServerError,
                        StandardResponse<MessagePlayerCreatureCreator>(
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
