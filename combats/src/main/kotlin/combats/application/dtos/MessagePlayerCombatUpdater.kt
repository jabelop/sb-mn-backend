package com.jatec.combats.application.dtos

import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatData
import combats.domain.options.UpdatePlayerCombatOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombatUpdater(val combat: Combat, val combatData: List<CombatData>)