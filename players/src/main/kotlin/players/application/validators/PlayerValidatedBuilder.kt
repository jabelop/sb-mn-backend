package com.jatec.players.application.validators

import com.jatec.players.domain.model.Player
import com.jatec.players.domain.value_objects.ValidName
import com.jatec.players.domain.value_objects.ValidUUID

class PlayerValidatedBuilder {
    companion object {
        fun buildValidatePlayer(player: Player): Player {
            return Player(
                id = ValidUUID(player.id).validatedUuid.toString(),
                name = ValidName(player.name).validatedName!!
                )

        }
    }
}