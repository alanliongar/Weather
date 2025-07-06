package com.example.weather

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import java.util.*
import java.time.*
import java.time.format.*


fun main() {
    println(getBoxSizes(0, 22))
    /*val date = "2025-07-02T18:30"
    val actual = getHour(date)
    println(actual)*/
}

fun getBoxSizes(tempMin: Int, tempMax: Int): Pair<Float, Float> {
    val box1: Int
    val box2: Int
    box2 = (tempMax - tempMin) * 10 / 7
    box1 = (100 - box2) / 2

    return Pair((box1 + box2).toFloat() / 100, ((100 * box1) / (box1 + box2)).toFloat() / 100)
}

fun getBarColor(avgTemp: Int): Color {
    val minTemp = 0
    val maxTemp = 40
    val clamped = avgTemp.coerceIn(minTemp, maxTemp)
    val fraction = (clamped - minTemp).toFloat() / (maxTemp - minTemp)

    return lerp(Color.Blue, Color.Red, fraction)
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

