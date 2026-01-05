package com.jatec.players.application.dtos

import com.jatec.creatures.domain.model.PlayerCreature
import com.jatec.players.domain.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayersCreatures(
    var players: List<Player>?,
    var creatures: List<PlayerCreature>?
)