package com.jatec.combats.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerCombatData(
    val player1Id: String,
    val player2Id: String,
    val combatData: List<CombatData>
)