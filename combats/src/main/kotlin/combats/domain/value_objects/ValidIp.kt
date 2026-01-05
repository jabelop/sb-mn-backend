package com.jatec.combats.domain.value_objects

import com.jatec.combats.domain.exceptions.InvalidName

class ValidIp(ip: String) {
    var validatedIp: String = ""
    init {
        if (!Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|\$)){4}$").containsMatchIn(ip))
            throw InvalidName("Invalid ip: $ip.")
        validatedIp = ip
    }
}