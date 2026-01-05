package com.jatec.combats.domain.value_objects

import com.jatec.combats.domain.exceptions.InvalidUUID
import java.util.UUID
import java.util.UUID.fromString

class ValidUUID(uuid: String?) {
    var validatedUuid: UUID? = null
    init {
       try {
           if (uuid != null) validatedUuid = fromString(uuid)
       } catch (e: Exception) {
           throw InvalidUUID("Invalid uuid: $uuid")
       }
    }
}