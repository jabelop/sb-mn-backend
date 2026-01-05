package com.jatec.creatures.domain.model

import kotlinx.serialization.Serializable
import java.nio.ByteBuffer

@Serializable
data class PlayerCreature(
    val id: String,
    val name: String,
    val creatureClass: String,
    val idPlayer: String,
    val level: Int,
    val xp: Int,
    val hp: Int,
    val speed: Int,
    val attack: Int
)

fun PlayerCreature.toByteBuffer(): ByteBuffer {
    val playerIdBytes = this.idPlayer.toByteArray(Charsets.UTF_8)
    val creatureIdBytes = this.id.toByteArray(Charsets.UTF_8)
    val nameBytes = this.name.toByteArray(Charsets.UTF_8)
    val creatureClassBytes = this.creatureClass.toByteArray(Charsets.UTF_8)
    val levelBytes = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this.level).array()
    val xpBytes = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this.xp).array()
    val hpBytes = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this.hp).array()
    val speedBytes = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this.speed).array()
    val attackBytes = ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this.attack).array()

    val buffer = ByteBuffer.allocate(
        playerIdBytes.size +
                creatureIdBytes.size +
                nameBytes.size +
                creatureClassBytes.size +
                levelBytes.size  +
                xpBytes.size +
                hpBytes.size +
                speedBytes.size +
                attackBytes.size +
                Int.SIZE_BYTES
    )

    buffer.put(playerIdBytes)
    buffer.put(creatureIdBytes)
    buffer.put(nameBytes)
    buffer.put(creatureClassBytes)
    buffer.put(levelBytes)
    buffer.put(xpBytes)
    buffer.put(hpBytes)
    buffer.put(speedBytes)
    buffer.put(attackBytes)

    buffer.flip() // Prepare the buffer for reading

    return buffer
}
