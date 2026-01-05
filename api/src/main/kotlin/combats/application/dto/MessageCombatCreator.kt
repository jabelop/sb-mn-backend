package com.jatec.combats.application.dto

import com.jatec.combats.domain.options.CreatePlayerCombatOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageCombatCreator(val options: CreatePlayerCombatOptions)