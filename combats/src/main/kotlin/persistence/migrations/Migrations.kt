@file:OptIn(ExperimentalDatabaseMigrationApi::class)
package com.jatec.persistence.migrations

import com.jatec.persistence.tables.CombatsTable
import com.jatec.persistence.tables.CombatsDataTable
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.vendors.currentDialectMetadata
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

const val URL = "jdbc:postgresql://localhost:5432/psql_db"
const val USER = "psql_user"
const val PASSWORD = "psql_password"
const val MIGRATIONS_DIRECTORY = "src/main/kotlin/persistence/migrations" // Location of migration scripts

fun initCombatsTable(database: Database) {
    transaction(database) {
        // combats table definition and initialization
        exec("DROP TABLE IF EXISTS combats")
        exec("CREATE TABLE IF NOT EXISTS combats (id UUID NOT NULL PRIMARY KEY, id_player1 UUID NOT NULL, id_player2 UUID NOT NULL, winner UUID, ip VARCHAR(15) NOT NULL, port INTEGER NOT NULL, started_at TIMESTAMP DEFAULT NOW(), updated_at TIMESTAMP DEFAULT NOW(), finished_at TIMESTAMP)")
        exec("CREATE INDEX Indx_combats_id_player1 ON combats (id_player1)")
        exec("CREATE INDEX Indx_combats_id_player2 ON combats(id_player2)")

        // player combats table definition and initialization
        exec("DROP TABLE IF EXISTS combats_data")
        exec("CREATE TABLE IF NOT EXISTS combats_data (id_combat UUID NOT NULL, id_player UUID NOT NULL, id UUID NOT NULL, name VARCHAR(100) NOT NULL, creature_class VARCHAR(80), level INTEGER NOT NULL, xp INTEGER NOT NULL, hp INTEGER NOT NULL, speed INTEGER NOT NULL, attack INTEGER NOT NULL, defense INTEGER DEFAULT 0, time_to_attack INTEGER DEFAULT 0, CONSTRAINT FK_id_combat_id FOREIGN KEY(id_combat) REFERENCES combats(id) )")
        exec("CREATE INDEX Indx_combats_data_id_combat ON combats_data (id_combat)")
        exec("CREATE INDEX Indx_combats_data_id_player ON combats_data (id_player)")

    }
}

fun generateMigrationScript() {
    // Generate a migration script in the specified path
    MigrationUtils.generateMigrationScript(
        CombatsTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "CombatsTable",
    )

    MigrationUtils.generateMigrationScript(
        CombatsDataTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "PlayerCombatsTable",
    )
}


fun main() {
    val h2db = Database.connect(
        url = URL,
        user = USER,
        password = PASSWORD
    )

    initCombatsTable(h2db)

    transaction(h2db) {
        println("*** Before migration ***")
        println("Primary key: ${currentDialectMetadata.existingPrimaryKeys(CombatsTable)[CombatsTable]}")

        // Generate a migration script
        generateMigrationScript()
    }

    transaction(h2db) {
        // Generate SQL statements required to align the database schema
        // against the current table definitions
        val statements = MigrationUtils.statementsRequiredForDatabaseMigration(
            CombatsTable
        )
        println(statements)

        // Disable logging
        MigrationUtils.statementsRequiredForDatabaseMigration(
            CombatsTable,
            withLogs = true
        )

        println("*** After migration ***")
        println("Primary key: ${currentDialectMetadata.existingPrimaryKeys(CombatsTable)[CombatsTable]}")

    }
}