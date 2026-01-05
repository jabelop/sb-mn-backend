package com.jatec.players.domain.value_objects

import com.jatec.players.domain.exceptions.InvalidName

class ValidName(name: String?) {
    var validatedName: String? = null
    init {
        if (name != null && (name.length < 5 || name.length > 100))
            throw InvalidName("Invalid name: $name. must be between 5 and 100 characters length")
        validatedName = name
    }
}