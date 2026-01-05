package com.jatec.players.domain.model

import kotlinx.serialization.Serializable
import java.nio.ByteBuffer
import java.util.UUID

@Serializable
data class Player(
    val id: String,
    val name: String
)

fun Player.toByteBuffer(): ByteBuffer {
    val nameBytes = this.name.toByteArray(Charsets.UTF_8)
    val idBytes = this.id.toByteArray(Charsets.UTF_8)
    val buffer = ByteBuffer.allocate(idBytes.size + nameBytes.size + Int.SIZE_BYTES)

    buffer.put(idBytes)
    buffer.put(nameBytes)

    buffer.flip() // Prepare the buffer for reading

    return buffer
}
