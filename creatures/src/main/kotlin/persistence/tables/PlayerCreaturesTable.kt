package com.jatec.persistence.tables

import org.jetbrains.exposed.v1.core.Table

object PlayerCreaturesTable : Table("player_creatures") {
    val id = uuid("id")
    val idPlayer = uuid("id_player")
    val name = varchar("name", NAME_LIMIT)
    val creatureClass = varchar("creature_class", CREATURE_CLASS_LIMIT)
    val level = integer("level")
    val xp = integer("xp")
    val hp = integer("hp")
    val speed = integer("speed")
    val attack = integer("attack")
    override val primaryKey = PrimaryKey(id)
}