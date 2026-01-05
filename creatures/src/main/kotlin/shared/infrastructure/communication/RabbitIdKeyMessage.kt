package com.jatec.shared.infrastructure.communication

import kotlinx.serialization.Serializable

@Serializable
data class RabbitIdKeyMessage<T>(val id: String, val routingKey: String, val payload: T)