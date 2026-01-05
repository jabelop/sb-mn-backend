package com.jatec.creatures.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class WhereOptions(
    val playerId: String?,
    val creatureId: String?,
    val enemyId: String?,
    val name: String?
)
