package com.example.weather

import com.google.gson.annotations.SerializedName

data class WeatherNextDaysDto(
    val current: Current,
    val daily: Daily
) {
    data class Current(
        @SerializedName("time") val time: String,
        @SerializedName("temperature_2m") val temperature: Float,
        @SerializedName("relative_humidity_2m") val humidity: Int,
        @SerializedName("rain") val rain: Float,
        @SerializedName("wind_speed_10m") val windSpeed: Float,
        @SerializedName("weather_Code") val weatherCode: Int,
    )

    data class Daily(
        @SerializedName("time") val time: List<String>,
        @SerializedName("weather_Code") val weatherCode: List<Int>,
        @SerializedName("temperature_2m_min") val temperatureMin: List<Float>,
        @SerializedName("temperature_2m_max") val temperatureMax: List<Float>
    )
}