package com.jatec.players.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class WhereOptions(
    val playerId: String?,
    val creatureId: String?,
    val name: String?
)
