package com.jatec.combats.application.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar

class DateUtils {
    companion object {
        fun nowString(): String {
            val today = Calendar.getInstance()
            val now = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(today.time)
            return now
        }

        fun unixTimeToDateString(unixTime: Long): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
            val date = java.util.Date(unixTime * 1000)
            return sdf.format(date)
        }

        fun timeStringToUnixDate(date: String): Long {
            val date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").parse(date)
            return date.time
        }

        fun nowUnixTime(): Long {
            return System.currentTimeMillis() / 1000
        }
    }
}