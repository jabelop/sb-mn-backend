package com.jatec.shared.infrastructure.communication

import kotlinx.serialization.Serializable

@Serializable
data class RabbitIdMessage<T>(val id: String, val payload: T)