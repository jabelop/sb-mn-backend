package com.jatec

import com.jatec.combats.application.service.CombatsService
import com.jatec.combats.domain.repository.CombatsRepository
import com.jatec.combats.infrastructure.repository.CombatsRepositoryPostgre
import com.jatec.shared.infrastructure.connection.ConnectionManagerKtor
import com.jatec.combats.infrastructure.router.configureCombatsQueues
import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import kotlinx.datetime.DateTimePeriod
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.net.InetAddress

fun main(args: Array<String>) {
    try {
        io.ktor.server.netty.EngineMain.main(args)
    } catch (e: Error) {
        println("[combats]: ${DateTimePeriod()} ${e.message}")
        println(e.stackTrace)
    }
}

fun Application.module() {
    // Koin Module
    val combatsModule = module {
        singleOf(::CombatsRepositoryPostgre) bind CombatsRepository::class
        singleOf(::CombatsService)
    }

    try {
        install(Koin) {
            modules(combatsModule)
        }
        val config = ApplicationConfig("application.yaml")
        ConnectionManagerKtor.currentIp = InetAddress.getLocalHost().hostAddress
        ConnectionManagerKtor.currentPort = config.port
        configureSockets()
        configureRouting()
        configureDatabases(ConfigManager(config))
        configureFrameworks(ConfigManager(config))
        configureCombatsQueues(ConfigManager(config))
    } catch (e: Error) {
        log.error("Error: $e")
    }
}
