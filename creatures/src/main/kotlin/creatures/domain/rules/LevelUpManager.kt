package com.jatec.creatures.domain.rules

import com.jatec.creatures.domain.model.PlayerCreature

interface LevelUpManager {

    fun increaseXp(creature: PlayerCreature, xp: Int): PlayerCreature
}