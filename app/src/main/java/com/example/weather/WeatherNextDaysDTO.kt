package com.example.weather

import com.google.gson.annotations.SerializedName

data class WeatherNextDaysDTO(
    val current: Current,
    val daily: Daily
) {
    data class Current(
        @SerializedName("time") val time: String,
        @SerializedName("temperature_2m") val temperature: Float,
        @SerializedName("relative_humidity_2m") val humidity: Int,
        @SerializedName("rain") val rain: Float,
        @SerializedName("wind_speed_10m") val wind: Float,
        @SerializedName("weather_code") val weather: Int,
    )

    data class Daily(
        @SerializedName("time") val time: List<String>,
        @SerializedName("weather_code") val weatherCode: List<Int>,
        @SerializedName("temperature_2m_min") val temperatureMin: List<Float>,
        @SerializedName("temperature_2m_max") val temperatureMax: List<Float>
    )
}

val time = listOf("2025-07-05","2025-07-06","2025-07-07","2025-07-08","2025-07-09","2025-07-10","2025-07-11")
val weatherCodeList = listOf(45, 45, 45, 45, 45, 45, 45)
val temperatureMax = listOf(18.5, 16.5, 18.3, 20.5, 16.4, 18.2, 19.9)
val temperatureMin = listOf(7.0, 7.5, 8.5, 8.5, 5.9, 9.8, 8.7)