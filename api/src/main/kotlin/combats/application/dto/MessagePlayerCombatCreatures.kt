package com.jatec.combats.application.dto

import com.jatec.combats.domain.options.CombatDataOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombatCreatures(
    var creatures: List<CombatDataOptions>
)