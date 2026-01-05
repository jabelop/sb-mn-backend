package com.jatec.combats.domain.value_objects

import com.jatec.combats.domain.exceptions.InvalidDateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ValidDateTime(dateTime: String) {
    var validatedDateTime: String = ""
    init {
        val sdf = SimpleDateFormat("YYYY-MM-DDTHH:mm:ss.sssZ", Locale.getDefault())
        sdf.isLenient = false // This ensures strict date validation

        try {
            sdf.parse(dateTime)
            validatedDateTime = dateTime
        } catch (_: ParseException) {
            throw InvalidDateTime("The date $dateTime is not valid. The format must be ISO format (YYYY-MM-DDTHH:mm:ss.sssZ)")
        }
    }
}