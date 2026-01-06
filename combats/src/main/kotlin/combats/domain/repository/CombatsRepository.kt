package com.jatec.combats.domain.repository

import com.jatec.combats.domain.model.Combat
import com.jatec.combats.domain.model.CombatCreatedData
import com.jatec.combats.domain.model.CombatData
import com.jatec.combats.domain.model.CreatePlayerCombatData
import com.jatec.combats.domain.options.WhereOptions
import combats.domain.options.UpdatePlayerCombatOptions

interface CombatsRepository {

    fun find(options: WhereOptions): List<Combat>

    fun findPlayerCombats(options: WhereOptions): List<CombatData>

    fun updatePlayerCombat(combat: Combat, combatData: List<CombatData>)

    fun createPlayerCombat(options: CreatePlayerCombatData): CombatCreatedData

    fun findCombatCreatedData(id: String): CombatCreatedData

    fun delete(combat: Combat): Boolean
}