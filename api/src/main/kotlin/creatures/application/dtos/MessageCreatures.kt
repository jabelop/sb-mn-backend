package com.jatec.creatures.application.dtos

import com.jatec.creatures.domain.model.Creature
import kotlinx.serialization.Serializable

@Serializable
data class MessageCreatures(
    var creatures: List<Creature>
)