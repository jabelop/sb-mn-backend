package com.jatec.combats.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerCombatOptions (
    val player1Id: String,
    val player2Id: String,
    val combatData: List<CombatDataOptions>,
    val isVsAi: Boolean
)