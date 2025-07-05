package com.example.weather

import java.util.*
import java.time.*
import java.time.format.*


fun main() {
    val date = "2025-07-02T18:30"
    val actual = getHour(date)
    println(actual)
}

fun getHour(isoDateTime: String): Int {
    return if ("T" in isoDateTime) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val dateTime = LocalDateTime.parse(isoDateTime, formatter)
        dateTime.hour
    } else {
        return 0
    }
}

fun getDay(isoDateTime: String): Int {
    return if ("T" in isoDateTime) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val dateTime = LocalDateTime.parse(isoDateTime, formatter)
        dateTime.dayOfMonth
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(isoDateTime, formatter)
        date.dayOfMonth
    }
}


fun formatToHourPeriod(isoDateTime: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val dateTime = LocalDateTime.parse(isoDateTime, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("hh a", Locale.ENGLISH)
    return dateTime.format(outputFormatter).lowercase()
}

fun formatToFullDate(isoDateTime: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val dateTime = LocalDateTime.parse(isoDateTime, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM, EEEE", Locale.ENGLISH)
    return dateTime.format(outputFormatter)
}

