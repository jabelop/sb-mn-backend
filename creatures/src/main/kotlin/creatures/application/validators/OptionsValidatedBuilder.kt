package com.jatec.creatures.application.validators

import com.jatec.creatures.application.dtos.MessageOptions
import com.jatec.creatures.domain.options.WhereOptions
import com.jatec.creatures.domain.value_objects.ValidName
import com.jatec.creatures.domain.value_objects.ValidUUID

class OptionsValidatedBuilder {

    companion object {
        fun buildValidatedOptions(options: MessageOptions): WhereOptions {
            return  WhereOptions(
                    name = if (options.where.name != null) ValidName(options.where.name).validatedName else null,
                    playerId = if (options.where.playerId != null) ValidUUID(options.where.playerId).validatedUuid?.toString() else null,
                    creatureId = if (options.where.creatureId != null) ValidUUID(options.where.creatureId).validatedUuid?.toString() else null,
                    enemyId = if (options.where.enemyId != null) ValidUUID(options.where.enemyId).validatedUuid?.toString() else null,
            )
        }
    }
}