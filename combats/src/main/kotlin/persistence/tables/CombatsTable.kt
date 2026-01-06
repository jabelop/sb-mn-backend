package com.jatec.persistence.tables

import org.jetbrains.exposed.v1.core.Table

const val IP_LENGTH = 15
const val DATETIME_LENGTH = 20

object CombatsTable : Table("combats") {
    val id = uuid("id")
    val idPlayer1 = uuid("id_player1")
    val idPlayer2 = uuid("id_player2")
    val winner = uuid("winner").nullable()
    val ip = varchar("ip", IP_LENGTH)
    val port = integer("port")
    val startedAt = varchar("started_at", DATETIME_LENGTH)
    val updatedAt = varchar("updated_at", DATETIME_LENGTH)
    val finishedAt = long("finished_at").nullable()
    override val primaryKey = PrimaryKey(id)
}