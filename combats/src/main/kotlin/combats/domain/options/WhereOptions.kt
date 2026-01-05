package com.jatec.combats.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class WhereOptions(
    val playerId: String?,
    val combatId: String?,
)
