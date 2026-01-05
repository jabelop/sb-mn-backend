package com.jatec.combats.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CombatData(
    val idCombat: String,
    val idPlayer: String,
    val id: String,
    val name: String,
    val creatureClass: String,
    val level: Int,
    val xp: Int,
    var hp: Int,
    val speed: Int,
    val attack: Int,
    var defense: Int,
    var timeToAttack: Int
)