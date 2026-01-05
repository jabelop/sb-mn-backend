package com.jatec.combats.application.validators

import com.jatec.combats.domain.value_objects.ValidUUID
import com.jatec.combats.domain.value_objects.ValidXPGain
import combats.domain.options.UpdatePlayerCombatOptions

class UpdatePlayerCombatOptionsValidatedBuilder {

    companion object {
        fun buildValidatedUpdatePlayerCombatOptions(options: UpdatePlayerCombatOptions): UpdatePlayerCombatOptions {
            return UpdatePlayerCombatOptions(
                combatId = ValidUUID(options.combatId.toString()).validatedUuid.toString(),
                xp = ValidXPGain(options.xp).validatedXp!!
            )
        }
    }
}