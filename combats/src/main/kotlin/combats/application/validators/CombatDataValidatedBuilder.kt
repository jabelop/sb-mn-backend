package com.jatec.combats.application.validators

import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.domain.options.CombatDataOptions
import com.jatec.combats.domain.value_objects.ValidName
import com.jatec.combats.domain.value_objects.ValidUUID
import combats.domain.value_objects.ValidCreatureClass
import java.util.UUID

class CombatDataValidatedBuilder {

    companion object {
        fun buildValidatedCombatData(options: List<CombatDataOptions>): List<CombatData> {
            val id = UUID.randomUUID().toString()
            return options.map { option ->
                CombatData(
                    idCombat = id,
                    idPlayer = ValidUUID(option.idPlayer).validatedUuid.toString(),
                    id = ValidUUID(option.id).validatedUuid.toString(),
                    name = ValidName(option.name).validatedName!!,
                    creatureClass = ValidCreatureClass(option.creatureClass).validatedClass,
                    level = option.level,
                    xp = option.xp,
                    hp = option.hp,
                    speed = option.speed,
                    attack = option.attack,
                    defense = 0,
                    timeToAttack = 1,
                )
            }
        }
    }
}