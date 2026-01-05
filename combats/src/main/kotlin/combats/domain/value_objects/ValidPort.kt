package com.jatec.combats.domain.value_objects

import com.jatec.combats.domain.exceptions.InvalidPort

const val MIN_PORT_ALLOWED = 3000
const val MAX_PORT_ALLOWED = 64000

class ValidPort(port: Int) {
    var validatedPort: Int = 0
    init {
        if (port < MIN_PORT_ALLOWED || port > MAX_PORT_ALLOWED) throw InvalidPort("Invalid port: $port.")
        validatedPort = port
    }
}