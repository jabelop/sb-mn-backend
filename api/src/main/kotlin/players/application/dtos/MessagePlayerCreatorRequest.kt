package com.jatec.players.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreatorRequest(
    var name: String
)