package com.jatec.combats.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class CombatDataOptions (
    val idPlayer: String,
    val id: String,
    val name: String,
    val creatureClass: String,
    val level: Int,
    val xp: Int,
    val hp: Int,
    val speed: Int,
    val attack: Int
)