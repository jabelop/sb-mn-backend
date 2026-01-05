package com.jatec.creatures.domain.value_objects

import com.jatec.creatures.domain.exceptions.InvalidCreatureClass
import com.jatec.creatures.domain.model.CreatureClass

class ValidCreatureClass(creatureClass: String) {
    var validatedClass: String = ""
    init {
        if (!CreatureClass.isValidClass(creatureClass))
            throw InvalidCreatureClass("Invalid creature class: $creatureClass. must be one of (${CreatureClass.classes.joinToString()})")
        validatedClass = creatureClass
    }
}