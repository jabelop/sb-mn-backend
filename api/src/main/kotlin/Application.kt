package com.jatec

import com.jatec.combats.infrastructure.response.CombatsResponseHandlerManager
import com.jatec.creatures.infrastructure.response.CreaturesResponseHandlerManager
import com.jatec.players.infrastructure.response.PlayersResponseHandlerManager
import com.jatec.shared.infrastructure.communication.ResponseCodeManagerHttp
import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    try {
    io.ktor.server.netty.EngineMain.main(args)
    } catch(e: Exception) {
        println(e.message)
        e.printStackTrace()
    }
}

fun Application.module() {
    try {
        install(CORS) {
            anyHost()
        }
        log.info("Starting application module")
        val config = ApplicationConfig("application.yaml")
        val responseCodeManager = ResponseCodeManagerHttp<HttpStatusCode>()
        val playersResponseHandlerManager = PlayersResponseHandlerManager()
        val creaturesResponseHandlerManager = CreaturesResponseHandlerManager()
        val combatsResponseHandlerManager = CombatsResponseHandlerManager()
        configureFrameworks(
            ConfigManager(config),
            responseCodeManager,
            playersResponseHandlerManager,
            creaturesResponseHandlerManager,
            combatsResponseHandlerManager
        )
        routing {
            swaggerUI(path = "swagger", swaggerFile = "docs/documentation.yaml")
        }
    } catch (e: Exception) {
        println("Application module error")
        println(e.message)
        e.printStackTrace()
    }
}
