package com.jatec.creatures.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreatureCreator(
    var playerId: String,
    var creatureId: String
)