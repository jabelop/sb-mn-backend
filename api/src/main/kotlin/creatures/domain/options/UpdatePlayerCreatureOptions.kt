package com.jatec.creatures.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlayerCreatureOptions (val creatureId: String, val xp: Int)