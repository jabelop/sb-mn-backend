package com.jatec.creatures.domain.value_objects

import com.jatec.creatures.domain.exceptions.InvalidName

class ValidName(name: String?) {
    var validatedName: String? = null
    init {
        if (name != null && (name.length < 5 || name.length > 80))
            throw InvalidName("Invalid name: $name. must be between 5 and 80 characters length")
        validatedName = name
    }
}