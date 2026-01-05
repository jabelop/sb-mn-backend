package com.jatec.players.application.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MessagePlayerCreator(
    var id: String,
    var name: String
)