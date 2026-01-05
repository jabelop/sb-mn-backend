package com.jatec.players.application.dtos

import com.jatec.players.domain.options.WhereOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageFinder(
    var where: WhereOptions
)