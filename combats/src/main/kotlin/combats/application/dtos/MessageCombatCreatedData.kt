package com.jatec.combats.application.dtos

import com.jatec.combats.domain.model.CombatCreatedData
import kotlinx.serialization.Serializable

@Serializable
data class MessageCombatCreatedData(val combatCreatedData: CombatCreatedData, val isVsAi: Boolean, val playerId: String)