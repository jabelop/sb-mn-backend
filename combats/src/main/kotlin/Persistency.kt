package com.jatec

import com.jatec.shared.infrastructure.config.ConfigManager
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.configureDatabases(configManager: ConfigManager) {

    Database.connect(
        url = configManager.urlBD,
        user = configManager.userDB,
        password = configManager.passwordDB
    )
}