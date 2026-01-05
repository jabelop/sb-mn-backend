package com.jatec.shared.application.dto.creatures

import com.jatec.shared.domain.options.creatures.WhereOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageFinder(
    var where: WhereOptions
)