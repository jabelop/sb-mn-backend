package com.jatec.combats.application.dtos

import com.jatec.combats.domain.options.WhereOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageOptions(
    var where: WhereOptions
)
