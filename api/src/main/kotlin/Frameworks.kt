package com.jatec

import com.jatec.combats.infrastructure.router.configureCombatsQueues
import com.jatec.combats.infrastructure.response.CombatsResponseHandlerManager
import com.jatec.creatures.infrastructure.router.configureCreaturesQueues
import com.jatec.creatures.infrastructure.response.CreaturesResponseHandlerManager
import com.jatec.players.infrastructure.router.configurePlayerQueues
import com.jatec.players.infrastructure.response.PlayersResponseHandlerManager
import com.jatec.shared.application.communication.ResponseCodeManager
import com.jatec.shared.infrastructure.config.ConfigManager
import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun Application.configureFrameworks(
    configManager: ConfigManager,
    responseHandlerManager: ResponseCodeManager<HttpStatusCode>,
    playerResponseCodeManager: PlayersResponseHandlerManager,
    creaturesResponseHandlerManager: CreaturesResponseHandlerManager,
    combatsResponseHandlerManager: CombatsResponseHandlerManager
) {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> log.error("ExceptionHandler got $throwable") }
    val rabbitMQScope = CoroutineScope(SupervisorJob() + exceptionHandler)

    install(RabbitMQ) {
        uri = configManager.uriRabbitMQ
        defaultConnectionName = "default-connection"
        dispatcherThreadPollSize = 4
        tlsEnabled = false
        scope = rabbitMQScope // custom scope, default is the one provided by Ktor
    }
    
    rabbitmq {
        queueBind {
            queue = "dlq"
            exchange = "dlx"
            routingKey = "dlq-dlx"
            exchangeDeclare {
                exchange = "dlx"
                type = "direct"
            }
            queueDeclare {
                queue = "dlq"
                durable = true
            }
        }
    }

    install(ContentNegotiation) {
        json()
    }

    configurePlayerQueues(configManager, responseHandlerManager, playerResponseCodeManager)
    configureCreaturesQueues(configManager, responseHandlerManager, creaturesResponseHandlerManager)
    configureCombatsQueues(configManager, responseHandlerManager, combatsResponseHandlerManager)

}
