package com.jatec.players.application.builders

import com.jatec.players.application.dtos.MessagePlayerCreator
import com.jatec.players.domain.model.Player

class PlayerBuilder {

    companion object {
        fun buildPlayerFromMessagePlayerCreator(messagePlayerCreator: MessagePlayerCreator): Player {
            return Player(
                id = messagePlayerCreator.id,
                name = messagePlayerCreator.name
            )
        }
    }
}