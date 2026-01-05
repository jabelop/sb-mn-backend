package com.jatec.combats.domain.value_objects

import com.jatec.combats.domain.exceptions.InvalidMatch

data class ValidatedMatch(val idPlayer1: String, val idPlayer2: String)

class ValidMatch(idPlayer1: String, idPlayer2: String) {
    var validatedMatch: ValidatedMatch? = null
    init {
        if (idPlayer1 != idPlayer2)
            throw InvalidMatch("The players can not be the same.")
        validatedMatch = ValidatedMatch(idPlayer1, idPlayer2)
    }
}