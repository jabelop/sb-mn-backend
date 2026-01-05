package com.jatec.combats.application.dtos

import com.jatec.combats.domain.model.CombatData
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombats(
    var combatData: List<CombatData>
)