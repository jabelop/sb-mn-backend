package com.jatec.creatures.domain.repository

import com.jatec.creatures.domain.model.Creature
import com.jatec.creatures.domain.model.PlayerCreature
import com.jatec.creatures.domain.options.CreatePlayerCreatureOptions
import com.jatec.creatures.domain.options.WhereOptions
import creatures.domain.options.UpdatePlayerCreatureOptions

interface CreaturesRepository {

    fun find(options: WhereOptions): List<Creature>

    fun findPlayerCreatures(options: WhereOptions): List<PlayerCreature>

    fun updatePlayerCreature(options: UpdatePlayerCreatureOptions): PlayerCreature

    fun createPlayerCreature(options: CreatePlayerCreatureOptions): Boolean

    fun delete(creature: Creature): Boolean
}