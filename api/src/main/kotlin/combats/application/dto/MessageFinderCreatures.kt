package com.jatec.combats.application.dto

import com.jatec.shared.domain.options.creatures.WhereOptions
import kotlinx.serialization.Serializable

@Serializable
data class MessageFinderCreatures(
    var where: WhereOptions
)