@file:OptIn(ExperimentalDatabaseMigrationApi::class)
package com.jatec.persistence.migrations

import com.jatec.persistence.tables.PlayersTable
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.vendors.currentDialectMetadata
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

const val URL = "jdbc:postgresql://localhost:5432/psql_db"
const val USER = "psql_user"
const val PASSWORD = "psql_password"
const val MIGRATIONS_DIRECTORY = "src/main/kotlin/persistence/migrations" // Location of migration scripts

fun initPlayersTable(database: Database) {
    transaction(database) {
        exec("DROP TABLE IF EXISTS Players")
        exec("CREATE TABLE IF NOT EXISTS Players (id UUID NOT NULL PRIMARY KEY, name VARCHAR(100) NOT NULL)")
        exec("INSERT INTO Players (id, name) VALUES ('05fb3246-9387-4d04-a27f-fa0107c33883', 'player one')")
        exec("INSERT INTO Players (id, name) VALUES ('05f03246-9c87-4d04-a27f-fa0107c33883', 'ai')")
    }
}

fun generateMigrationScript() {
    // Generate a migration script in the specified path
    MigrationUtils.generateMigrationScript(
        PlayersTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "PlayersTable",
    )
}


fun main() {
    val h2db = Database.connect(
        url = URL,
        user = USER,
        password = PASSWORD
    )


    initPlayersTable(h2db)

    transaction(h2db) {
        println("*** Before migration ***")
        println("Primary key: ${currentDialectMetadata.existingPrimaryKeys(PlayersTable)[PlayersTable]}")

        // Generate a migration script
        generateMigrationScript()
    }

    transaction(h2db) {
        // Generate SQL statements required to align the database schema
        // against the current table definitions
        val statements = MigrationUtils.statementsRequiredForDatabaseMigration(
            PlayersTable
        )
        println(statements)

        // Disable logging
        MigrationUtils.statementsRequiredForDatabaseMigration(
            PlayersTable,
            withLogs = false
        )


    }

    transaction(h2db) {
        println("*** After migration ***")
        println("Primary key: ${currentDialectMetadata.existingPrimaryKeys(PlayersTable)[PlayersTable]}")

    }
}