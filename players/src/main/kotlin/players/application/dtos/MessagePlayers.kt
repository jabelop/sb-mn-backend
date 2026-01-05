package com.jatec.players.application.dtos

import com.jatec.players.domain.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayers(
    var players: List<Player>
)