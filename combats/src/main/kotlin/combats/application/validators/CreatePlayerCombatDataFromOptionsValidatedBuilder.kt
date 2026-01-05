package com.jatec.combats.application.validators

import com.jatec.combats.domain.exceptions.SamePlayersException
import com.jatec.combats.domain.model.CreatePlayerCombatData
import com.jatec.combats.domain.options.CreatePlayerCombatOptions
import com.jatec.combats.domain.value_objects.ValidUUID

class CreatePlayerCombatDataFromOptionsValidatedBuilder {

    companion object {
        fun buildValidatedCreatePlayerCombatOptions(options: CreatePlayerCombatOptions): CreatePlayerCombatData {
            if (options.player1Id.equals(options.player2Id)) throw SamePlayersException("The player ${options.player1Id} can not combat versus itself")
            println("Validated g ${options.player1Id} != ${options.player2Id}")
            return CreatePlayerCombatData(
                player1Id = ValidUUID(options.player1Id).validatedUuid.toString(),
                player2Id = ValidUUID(options.player2Id).validatedUuid.toString(),
                combatData = CombatDataValidatedBuilder.buildValidatedCombatData(options.combatData)
            )
        }
    }
}