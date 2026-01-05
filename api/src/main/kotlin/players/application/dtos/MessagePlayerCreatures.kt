package com.jatec.players.application.dtos

import com.jatec.creatures.domain.model.PlayerCreature
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreatures(
    var creatures: List<PlayerCreature>
)