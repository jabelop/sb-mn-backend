package com.jatec.creatures.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Creature(
    val id: String,
    val name: String,
    val creatureClass: String,
    val level: Int,
    val xp: Int,
    val hp: Int,
    val speed: Int,
    val attack: Int
)
