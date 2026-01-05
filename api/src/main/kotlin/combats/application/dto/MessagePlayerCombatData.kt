package com.jatec.combats.application.dto

import com.jatec.combats.domain.model.CombatData
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombatData(
    var combatData: List<CombatData>
)