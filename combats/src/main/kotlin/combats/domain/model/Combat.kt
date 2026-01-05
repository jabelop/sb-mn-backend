package com.jatec.combats.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Combat(
    val id: String,
    val idPlayer1: String,
    val idPlayer2: String,
    val winner: String?,
    val ip: String,
    val port: Int,
    val startedAt: String?,
    val updatedAt: String?,
    val finishedAt: String?
)
