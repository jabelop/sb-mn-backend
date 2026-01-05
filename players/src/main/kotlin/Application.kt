package com.jatec

import com.jatec.players.application.service.PlayersService
import com.jatec.players.domain.repository.PlayersRepository
import com.jatec.players.infrastructure.repository.PlayersRepositoryPostgre
import com.jatec.players.infrastructure.router.configurePlayerQueues
import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Koin Module
    val playersModule = module {
        singleOf(::PlayersRepositoryPostgre) bind PlayersRepository::class
        singleOf(::PlayersService)
    }
    try {
        install(Koin) {
            modules(playersModule)
        }
        val config = ApplicationConfig("application.yaml")
        configureDatabases(ConfigManager(config))
        configureFrameworks(ConfigManager(config))
        configurePlayerQueues(ConfigManager(config))
    } catch (e: Exception) {
        log.error("Error app module: ${e.message}")
        e.printStackTrace()
    }
}
