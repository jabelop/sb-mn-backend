package com.jatec.creatures.application.validators

import com.jatec.creatures.domain.value_objects.ValidUUID
import com.jatec.creatures.domain.value_objects.ValidXPGain
import creatures.domain.options.UpdatePlayerCreatureOptions

class UpdatePlayerCreatureOptionsValidatedBuilder {

    companion object {
        fun buildValidatedUpdatePlayerCreatureOptions(options: UpdatePlayerCreatureOptions): UpdatePlayerCreatureOptions {
            return UpdatePlayerCreatureOptions(
                creatureId = ValidUUID(options.creatureId.toString()).validatedUuid.toString(),
                xp = ValidXPGain(options.xp).validatedXp!!
            )
        }
    }
}