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
    val hp: Int,
    val speed: Int,
    val attack: Int,
    val defense: Int,
    val timeToAttack: Int

)