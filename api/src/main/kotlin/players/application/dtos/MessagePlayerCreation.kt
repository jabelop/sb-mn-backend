package com.jatec.players.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreation(
    var error: Boolean,
    var message: String
)