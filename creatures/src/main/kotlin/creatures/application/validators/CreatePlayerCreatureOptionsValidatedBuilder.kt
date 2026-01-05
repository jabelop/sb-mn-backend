package com.jatec.creatures.application.validators

import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import com.jatec.creatures.domain.value_objects.ValidUUID

class CreatePlayerCreatureOptionsValidatedBuilder {

    companion object {
        fun buildValidatedCreatePlayerCreatureOptions(options: CreatePlayerCreatureOptions): CreatePlayerCreatureOptions {
            return CreatePlayerCreatureOptions(
                playerId = ValidUUID(options.playerId.toString()).validatedUuid.toString(),
                creatureId = ValidUUID(options.creatureId.toString()).validatedUuid.toString(),
            )
        }
    }
}