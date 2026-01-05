package com.jatec.combats.application.dtos

import combats.domain.options.UpdatePlayerCombatOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCombatUpdater(val options: UpdatePlayerCombatOptions)