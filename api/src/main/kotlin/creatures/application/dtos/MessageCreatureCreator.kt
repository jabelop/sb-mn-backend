package com.jatec.creatures.application.dtos

import kotlinx.serialization.Serializable


@Serializable
data class MessageCreatureCreator(val options: MessagePlayerCreatureCreator)