package com.jatec.combats.application.dto

import com.jatec.combats.domain.model.CombatCreatedData
import kotlinx.serialization.Serializable

@Serializable
class MessageCombatCreatedData(val combatCreatedData: CombatCreatedData, val isVsAi: Boolean, val playerId: String)