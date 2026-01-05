package com.jatec.players.application.validators

import com.jatec.players.application.dtos.MessageOptions
import com.jatec.players.domain.options.WhereOptions
import com.jatec.players.domain.value_objects.ValidName
import com.jatec.players.domain.value_objects.ValidUUID

class OptionsValidatedBuilder {

    companion object {
        fun buildValidatedOptions(options: MessageOptions): WhereOptions {
            return  WhereOptions(
                    name = ValidName(options.where.name).validatedName,
                    playerId = ValidUUID(options.where.playerId).validatedUuid?.toString(),
                    creatureId = ValidUUID(options.where.creatureId).validatedUuid?.toString()
            )
        }
    }
}