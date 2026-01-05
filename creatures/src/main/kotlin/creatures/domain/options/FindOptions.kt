package com.jatec.creatures.domain.options

import kotlinx.serialization.Serializable

@Serializable
data class FindOptions (
    val where: WhereOptions,
)