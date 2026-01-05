package com.jatec.creatures.domain.rules

import com.jatec.creatures.domain.constants.ATTACK_INCREASE
import com.jatec.creatures.domain.constants.HP_INCREASE
import com.jatec.creatures.domain.constants.SPEED_INCREASE
import com.jatec.creatures.domain.model.PlayerCreature

class LevelUpManagerImpl: LevelUpManager {
    override fun increaseXp(
        creature: PlayerCreature,
        xp: Int
    ): PlayerCreature {
        val newXp = xp + creature.xp
        val newLevel = calculateNewLevel(newXp)
        val newHp = calculateNewHp(creature.hp)
        val newSpeed = calculateNewSpeed(creature.speed)
        val newAttack = calculateNewAttack(creature.attack)
        return PlayerCreature(
            id = creature.id,
            idPlayer = creature.idPlayer,
            creatureClass = creature.creatureClass,
            name = creature.name,
            level = newLevel,
            xp = newXp,
            hp = newHp,
            speed = newSpeed,
            attack = newAttack
        )
    }

    fun calculateNewLevel(xp: Int): Int {
        val newLevelString = "$xp".subSequence(0, "$xp".length -2).toString()
        return if ("$xp".length < 3) 0 else newLevelString.toInt()

    }

    fun calculateNewHp(hp: Int): Int {
        return hp + HP_INCREASE
    }

    fun calculateNewSpeed(speed: Int): Int {
        return speed +  SPEED_INCREASE
    }

    fun calculateNewAttack(attack: Int): Int {
        return attack +  ATTACK_INCREASE
    }
}