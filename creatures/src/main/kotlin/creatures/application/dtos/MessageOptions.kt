package com.jatec.creatures.application.dtos

import com.jatec.creatures.domain.options.WhereOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageOptions(
    var where: WhereOptions
)
