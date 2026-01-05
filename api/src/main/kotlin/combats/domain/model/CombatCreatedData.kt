package com.jatec.combats.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CombatCreatedData(val combat: Combat, val combatData: List<CombatData>)