package com.jatec.persistence.tables

import org.jetbrains.exposed.v1.core.Table

const val IP_LENGTH = 15
const val DATETIME_LENGTH = 20

object CombatsTable : Table("combats") {
    val id = uuid("id")
    val idPlayer1 = uuid("id_player1")
    val idPlayer2 = uuid("id_player2")
    val winner = uuid("winner")
    val ip = varchar("ip", IP_LENGTH)
    val port = integer("port")
    val startedAt = varchar("started_at", DATETIME_LENGTH)
    val updatedAt = varchar("updated_at", DATETIME_LENGTH)
    val finishedAt = varchar("finished_at", DATETIME_LENGTH)
    override val primaryKey = PrimaryKey(id)
}