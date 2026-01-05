package com.jatec.combats.application.dto

import com.jatec.combats.domain.options.WhereOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageFinder(
    var where: WhereOptions
)