package com.jatec.creatures.application.dtos

import com.jatec.creatures.domain.options.UpdatePlayerCreatureOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreatureUpdater(val options: UpdatePlayerCreatureOptions)