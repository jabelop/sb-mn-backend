package com.jatec.creatures.application.dtos

import creatures.domain.options.UpdatePlayerCreatureOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreatureUpdater(val options: UpdatePlayerCreatureOptions)