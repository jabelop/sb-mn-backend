package com.jatec.combats.application.service

import com.jatec.combats.application.dtos.MessageCombatCreatedData
import com.jatec.combats.application.validators.CombatValidatedBuilder
import com.jatec.combats.application.validators.CreatePlayerCombatDataFromOptionsValidatedBuilder
import com.jatec.combats.application.validators.OptionsValidatedBuilder
import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.domain.options.CreatePlayerCombatOptions
import com.jatec.combats.domain.options.WhereOptions
import com.jatec.combats.domain.repository.CombatsRepository
import com.jatec.combats.domain.value_objects.ValidUUID

class CombatsService(combatsRepository: CombatsRepository) {
    val repository = combatsRepository

    fun find(options: WhereOptions): List<Combat> {
        return repository.find(OptionsValidatedBuilder.Companion.buildValidatedOptions(options))
    }

    fun findPlayerCombats(options: WhereOptions): List<CombatData> {
        return repository.findPlayerCombats(OptionsValidatedBuilder.Companion.buildValidatedOptions(options))
    }
    fun findCombatCreatedData(id: String): CombatCreatedData {
        return repository.findCombatCreatedData(ValidUUID(id).validatedUuid.toString())
    }

    fun updatePlayerCombat(combat: Combat, combatData: List<CombatData>) {
        return repository.updatePlayerCombat(
            combat = combat,
            combatData = combatData
        )
    }

    fun create(options: CreatePlayerCombatOptions): MessageCombatCreatedData {
       return MessageCombatCreatedData(
           combatCreatedData = repository.createPlayerCombat(
               CreatePlayerCombatDataFromOptionsValidatedBuilder.Companion.buildValidatedCreatePlayerCombatOptions(
                   options
               ),
           ),
           isVsAi = options.isVsAi,
           playerId = options.player1Id
       )
    }

    fun delete(combat: Combat): Boolean {
        return repository.delete(CombatValidatedBuilder.Companion.buildValidatedCombat(combat))
    }

}