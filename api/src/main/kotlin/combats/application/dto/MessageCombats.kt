package com.jatec.combats.application.dto

import com.jatec.combats.domain.model.Combat
import kotlinx.serialization.Serializable

@Serializable
data class MessageCombats(
    var combats: List<Combat>
)