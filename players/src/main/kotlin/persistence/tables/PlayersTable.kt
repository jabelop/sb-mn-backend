package com.jatec.persistence.tables

import org.jetbrains.exposed.v1.core.Table

const val NAME_LIMIT = 100

object PlayersTable : Table("players") {
    val id = uuid("id")
    val name = varchar("name", NAME_LIMIT)

    override val primaryKey = PrimaryKey(id)
}