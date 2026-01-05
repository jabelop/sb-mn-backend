package com.jatec.persistence.tables

import org.jetbrains.exposed.v1.core.Table

const val NAME_LIMIT = 100
const val COMBAT_CLASS_LIMIT = 80

object CombatsDataTable : Table("combats_data") {
    val idCombat = uuid("id_combat")
    val idPlayer = uuid("id_player")
    val id = uuid("id")
    val name = varchar("name", NAME_LIMIT)
    val creatureClass = varchar("creature_class", COMBAT_CLASS_LIMIT)
    val level = integer("level")
    val xp = integer("xp")
    val hp = integer("hp")
    val speed = integer("speed")
    val attack = integer("attack")
    val defense = integer("defense")
    val timeToAttack = integer("time_to_attack")
}