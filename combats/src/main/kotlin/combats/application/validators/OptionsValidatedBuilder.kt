package com.jatec.combats.application.validators

import com.jatec.combats.domain.options.WhereOptions
import com.jatec.combats.domain.value_objects.ValidUUID

class OptionsValidatedBuilder {

    companion object {
        fun buildValidatedOptions(options: WhereOptions): WhereOptions {
            return WhereOptions(
                    playerId = ValidUUID(options.playerId).validatedUuid?.toString(),
                    combatId = ValidUUID(options.combatId).validatedUuid?.toString()
            )
        }
    }
}