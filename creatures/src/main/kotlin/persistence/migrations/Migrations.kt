@file:OptIn(ExperimentalDatabaseMigrationApi::class)
package com.jatec.persistence.migrations

import com.jatec.persistence.tables.CreaturesTable
import com.jatec.persistence.tables.PlayerCreaturesTable
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.vendors.currentDialectMetadata
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils

const val URL = "jdbc:postgresql://localhost:5432/psql_db"
const val USER = "psql_user"
const val PASSWORD = "psql_password"
const val MIGRATIONS_DIRECTORY = "src/main/kotlin/persistence/migrations" // Location of migration scripts

fun initCreaturesTable(database: Database) {
    transaction(database) {
        // creatures table definition and initialization
        exec("DROP TABLE IF EXISTS creatures")
        exec("CREATE TABLE IF NOT EXISTS creatures (id UUID NOT NULL PRIMARY KEY, name VARCHAR(100) NOT NULL, creature_class VARCHAR(80), level INTEGER DEFAULT 0, xp INTEGER DEFAULT 0, hp INTEGER DEFAULT 100, speed INTEGER DEFAULT 10, attack INTEGER DEFAULT 4)")
        exec("INSERT INTO creatures (id, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4d04-a27f-fa0107c33883', 'orlok','defender', 0, 0, 100, 10, 5)")
        exec("INSERT INTO creatures (id, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4dc4-a27f-fa0107c33883', 'trinto','defender', 0, 0, 100, 10, 4)")
        exec("INSERT INTO creatures (id, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4d04-a25f-fa0107c33883', 'beinus','enchanter', 0, 0, 100, 10, 10)")
        exec("INSERT INTO creatures (id, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4dc4-a25f-fa0107c33883', 'floteh','enchanter', 0, 0, 100, 10, 12)")
        exec("INSERT INTO creatures (id, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4d04-a25f-fa010dc33883', 'colatin','warrior', 0, 0, 100, 10, 14)")
        exec("INSERT INTO creatures (id, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4dc4-a25f-fa0102c33883', 'gergus','warrior', 0, 0, 100, 10, 15)")

        // player creatures table definition and initialization
        exec("DROP TABLE IF EXISTS player_creatures")
        exec("CREATE TABLE IF NOT EXISTS player_creatures (id UUID NOT NULL, id_player UUID NOT NULL, name VARCHAR(100) NOT NULL, creature_class VARCHAR(80), level INTEGER DEFAULT 0, xp INTEGER DEFAULT 0, hp INTEGER DEFAULT 100, speed INTEGER DEFAULT 10, attack INTEGER DEFAULT 4)")
        exec("CREATE UNIQUE INDEX Indx_player_creatures_id ON player_creatures (id)")
        exec("CREATE INDEX Indx_player_creatures_id_player ON player_creatures (id_player)")

        // Init all creatures for AI
        // orlok
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('05fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'orlok','defender', 0, 0, 100, 10, 5)")
        exec("INSERT INTO player_creatures (id, id_player,name, creature_class, level, xp, hp, speed, attack) VALUES ('15fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'orlok','defender', 1, 105, 150, 14, 10)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('25fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'orlok','defender', 2, 215, 200, 18, 15)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('35fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'orlok','defender', 3, 301, 245, 21, 20)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('45fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'orlok','defender', 4, 408, 285, 24, 25)")
        // trinto
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('55fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'trinto','defender', 0, 0, 100, 10, 4)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('65fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'trinto','defender', 1, 105, 160, 13, 9)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('75fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'trinto','defender', 2, 215, 210, 17, 14)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('85fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'trinto','defender', 3, 301, 255, 20, 19)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('95fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'trinto','defender', 4, 408, 295, 23, 24)")
        // beinus
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('01fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'beinus','enchanter', 0, 0, 100, 10, 10)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('02fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'beinus','enchanter', 1, 105, 150, 14, 15)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('03fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'beinus','enchanter', 2, 215, 200, 18, 20)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('04fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'beinus','enchanter', 3, 301, 245, 21, 25)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('06fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'beinus','enchanter', 4, 408, 285, 24, 30)")
        // floteh
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('07fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'floteh','enchanter', 0, 0, 100, 10, 12)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('08fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'floteh','enchanter', 1, 105, 160, 13, 17)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('09fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'floteh','enchanter', 2, 215, 210, 17, 22)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('0afb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'floteh','enchanter', 3, 301, 255, 20, 27)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('0bfb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'floteh','enchanter', 4, 408, 295, 23, 32)")
        // colatin
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('0cfb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'colatin','warrior', 0, 0, 100, 10, 14)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('0dfb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'colatin','warrior', 1, 105, 150, 14, 19)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('0efb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'colatin','warrior', 2, 215, 200, 18, 24)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('0ffb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'colatin','warrior', 3, 301, 245, 21, 29)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('10fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'colatin','warrior', 4, 408, 285, 24, 34)")
        // gergus
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('11fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'gergus','warrior', 0, 0, 100, 10, 15)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('12fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'gergus','warrior', 1, 105, 160, 13, 20)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('13fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'gergus','warrior', 2, 215, 210, 17, 25)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('14fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'gergus','warrior', 3, 301, 255, 20, 30)")
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('16fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'ai'), 'gergus','warrior', 4, 408, 295, 23, 35)")

        // Init all creatures for player one
        //trinto
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('17fb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'player one'), 'trinto','defender', 0, 0, 100, 10, 4)")
        //colatin
        exec("INSERT INTO player_creatures (id, id_player, name, creature_class, level, xp, hp, speed, attack) VALUES ('1bfb3246-9387-4d04-a27f-fa0107c33883', (SELECT id FROM players where name = 'player one'), 'colatin','warrior', 0, 0, 100, 10, 14)")
    }
}

fun generateMigrationScript() {
    // Generate a migration script in the specified path
    MigrationUtils.generateMigrationScript(
        CreaturesTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "CreaturesTable",
    )

    MigrationUtils.generateMigrationScript(
        PlayerCreaturesTable,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = "PlayerCreaturesTable",
    )
}


fun main() {
    val h2db = Database.connect(
        url = URL,
        user = USER,
        password = PASSWORD
    )

    initCreaturesTable(h2db)

    transaction(h2db) {
        println("*** Before migration ***")
        println("Primary key: ${currentDialectMetadata.existingPrimaryKeys(CreaturesTable)[CreaturesTable]}")

        // Generate a migration script
        generateMigrationScript()
    }

    transaction(h2db) {
        // Generate SQL statements required to align the database schema
        // against the current table definitions
        val statements = MigrationUtils.statementsRequiredForDatabaseMigration(
            CreaturesTable
        )
        println(statements)

        // Disable logging
        MigrationUtils.statementsRequiredForDatabaseMigration(
            CreaturesTable,
            withLogs = true
        )

        println("*** After migration ***")
        println("Primary key: ${currentDialectMetadata.existingPrimaryKeys(CreaturesTable)[CreaturesTable]}")

    }
}