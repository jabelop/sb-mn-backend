package com.jatec.creatures.application.dtos

import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageCreatureCreator(val options: CreatePlayerCreatureOptions)