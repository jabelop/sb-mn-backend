package com.jatec

import com.jatec.creatures.application.service.CreaturesService
import com.jatec.creatures.domain.repository.CreaturesRepository
import com.jatec.creatures.domain.rules.LevelUpManager
import com.jatec.creatures.domain.rules.LevelUpManagerImpl
import com.jatec.creatures.infrastructure.repository.CreaturesRepositoryPostgre
import com.jatec.creatures.infrastructure.router.configureCreaturesQueues
import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import kotlinx.datetime.DateTimePeriod
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    try {
        io.ktor.server.netty.EngineMain.main(args)
    } catch (e: Error) {
        println("[Creatures]: ${DateTimePeriod()} ${e.message}")
        println(e.stackTrace)
    }
}

fun Application.module() {
    // Koin Module
    val creaturesModule = module {
        singleOf(::LevelUpManagerImpl) bind LevelUpManager::class
        singleOf(::CreaturesRepositoryPostgre) bind CreaturesRepository::class
        singleOf(::CreaturesService)
    }

    try {
        install(Koin) {
            modules(creaturesModule)
        }
        val config = ApplicationConfig("application.yaml")
        configureDatabases(ConfigManager(config))
        configureFrameworks(ConfigManager(config))
        configureCreaturesQueues(ConfigManager(config))
    } catch (e: Error) {
        log.error("Error: $e")
    }
}
