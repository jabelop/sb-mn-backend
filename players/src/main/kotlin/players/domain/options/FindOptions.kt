package com.jatec.players.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class FindOptions (
    val where: WhereOptions,
)