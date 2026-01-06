package com.jatec.combats.application.dtos

import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatData
import kotlinx.serialization.Serializable

@Serializable
data class MessageCombatRunning(
    val action: String,
    val status: String,
    val combatData: List<CombatData>,
    val combat: Combat,
    val next: String?
)