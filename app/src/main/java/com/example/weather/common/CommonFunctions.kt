package com.example.weather.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.example.weather.today.data.model.HourlyWeatherUiData
import com.example.weather.current.data.model.CurrentWeatherUiData
import com.example.weather.nextdays.data.model.DailyWeather
import com.example.weather.nextdays.data.model.WeatherNextDaysDTO
import com.example.weather.today.data.model.Weather
import com.example.weather.today.data.model.WeatherTodayDTO
import java.util.*
import java.time.*
import java.time.format.*

fun main() {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val formatted = now.format(formatter)
    println(getHour(formatted))
}

fun getBarSize(tempMin: Int, tempMax: Int): Pair<Float, Float> {
    val box1: Int
    val box2: Int
    box2 = (tempMax - tempMin) * 10 / 2
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

fun getWeekDay(isoDateTime: String): String {
    return if ("T" in isoDateTime) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val dateTime = LocalDateTime.parse(isoDateTime, formatter)
        dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(isoDateTime, formatter)
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.US)
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

fun getWeatherEmoji(weatherCode: Int): String {
    return when (weatherCode) {
        0 -> "☀️" // Clear sky
        in 1..3 -> "⛅" // Mainly clear, partly cloudy, and overcast
        45, 48 -> "\uD83E\uDD76" // Fog and depositing rime fog
        in 51..55 -> "🌦️" // Drizzle: Light, moderate, and dense intensity
        in 56..57 -> "🌧️❄️" // Freezing Drizzle: Light and dense intensity
        in 61..63 -> "🌧️" // Rain: Slight and moderate intensity
        65 -> "🌧️🌧️" // Rain: Heavy intensity
        in 66..67 -> "🌧️❄️" // Freezing Rain: Light and heavy intensity
        in 71..75 -> "❄️" // Snow fall: Slight, moderate, and heavy intensity
        77 -> "🌨️" // Snow grains
        in 80..81 -> "🌧️" // Rain showers: Slight and moderate
        82 -> "🌧️🌩️" // Rain showers: Violent
        85 -> "❄️" // Snow showers: Slight
        86 -> "❄️🌨️" // Snow showers: Heavy
        95 -> "⛈️" // Thunderstorm: Slight or moderate
        in 96..99 -> "⛈️⚡" // Thunderstorm with slight and heavy hail
        else -> "❓" // Unknown
    }
}

fun getWeatherDescription(weatherCode: Int): String {
    return when (weatherCode) {
        0 -> "Clear"
        1, 2, 3 -> "Cloudy"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Icy"
        61, 63, 65 -> "Rain"
        66, 67 -> "Icy"
        71, 73, 75, 77, 85, 86 -> "Snow"
        80, 81, 82 -> "Showers"
        95 -> "Thunder"
        96, 99 -> "Thunderstorm"
        else -> "Unknown"
    }
}


fun convertWeatherHourlyFromDTOToListHourlyWeather(
    weatherHourly: Weather.Hourly, days: Int = 1
): List<HourlyWeatherUiData> {
    val zoneId = ZoneId.of("America/Sao_Paulo")
    val now = ZonedDateTime.now(zoneId).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val formattedDate = now.format(formatter)
    val hourlyMap: List<HourlyWeatherUiData>
    if (days == 1) {
        hourlyMap = weatherHourly.time.indices.mapNotNull { index ->
            if (getHour(weatherHourly.time[index]) >= getHour(formattedDate) && getDay(
                    weatherHourly.time[index]
                ) == getDay(formattedDate)
            ) {
                HourlyWeatherUiData(
                    time = weatherHourly.time[index],
                    temperature = weatherHourly.temperature[index],
                    weatherCode = weatherHourly.weatherCode[index]
                )
            } else {
                null
            }
        }
    } else {
        hourlyMap = weatherHourly.time.indices.mapNotNull { index ->
            if (getDay(weatherHourly.time[index]) == getDay(formattedDate) + days - 1) {
                HourlyWeatherUiData(
                    time = weatherHourly.time[index],
                    temperature = weatherHourly.temperature[index],
                    weatherCode = weatherHourly.weatherCode[index]
                )
            } else {
                null
            }
        }
    }
    return hourlyMap
}

fun convertWeatherNextDaysDTOToListDailyWeather(
    weatherNextDaysDTO: WeatherNextDaysDTO
): List<DailyWeather> {
    val converted: List<DailyWeather> =
        weatherNextDaysDTO.daily.weatherCode.indices.map { index ->
            DailyWeather(
                time = getWeekDay(weatherNextDaysDTO.daily.time[index]),
                temperatureMax = weatherNextDaysDTO.daily.temperatureMax[index].toInt(),
                temperatureMin = weatherNextDaysDTO.daily.temperatureMin[index].toInt(),
                weatherCode = weatherNextDaysDTO.daily.weatherCode[index]
            )
        }
    return converted
}

fun convertWTDTOToCWInfo(weatherTodayDTO: WeatherTodayDTO.Current?): CurrentWeatherUiData {
    return CurrentWeatherUiData(
        "Sao Paulo",
        weatherTodayDTO?.weather ?: 0,
        weatherTodayDTO?.temperature ?: 0f,
        weatherTodayDTO?.wind ?: 0f,
        weatherTodayDTO?.humidity ?: 0,
        weatherTodayDTO?.rain ?: 0f,
        weatherTodayDTO?.time ?: "Error"
    )
}