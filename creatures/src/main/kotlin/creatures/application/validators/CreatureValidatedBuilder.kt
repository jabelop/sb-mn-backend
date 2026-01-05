package com.jatec.creatures.application.validators

import com.jatec.creatures.domain.model.Creature
import com.jatec.creatures.domain.value_objects.ValidCreatureClass
import com.jatec.creatures.domain.value_objects.ValidName
import com.jatec.creatures.domain.value_objects.ValidUUID

class CreatureValidatedBuilder {

    companion object {
        fun buildValidatedCreature(creature: Creature): Creature {
            return Creature(
                id = ValidUUID(creature.id.toString()).validatedUuid.toString(),
                name = ValidName(creature.name).validatedName!!,
                creatureClass = ValidCreatureClass(creature.creatureClass).validatedClass,
                level = creature.level,
                xp = creature.xp,
                hp = creature.hp,
                speed = creature.speed,
                attack = creature.attack
                )
        }
    }
}