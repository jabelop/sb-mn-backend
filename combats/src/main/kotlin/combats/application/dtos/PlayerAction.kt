package com.jatec.combats.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PlayerAction(val sourceId: String, val targetId: String, val action: String)