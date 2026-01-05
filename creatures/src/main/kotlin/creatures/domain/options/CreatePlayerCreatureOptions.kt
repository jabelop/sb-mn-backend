package com.jatec.creatures.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlayerCreatureOptions (val playerId: String, val creatureId: String)