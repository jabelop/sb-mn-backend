package com.jatec.combats.domain.value_objects

import com.jatec.combats.domain.exceptions.InvalidXPGain

class ValidXPGain(xp: Int) {
    var validatedXp: Int? = null
    val maxXPGainAllowed = 35
    init {

        if (xp > maxXPGainAllowed || xp < 1) throw InvalidXPGain("Invalid xp gain: $xp")
        validatedXp = xp



    }
}