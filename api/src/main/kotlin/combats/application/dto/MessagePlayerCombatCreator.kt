package com.jatec.combats.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombatCreator(
    val player1Id: String,
    val player2Id: String,
    val isVsAi: Boolean
)