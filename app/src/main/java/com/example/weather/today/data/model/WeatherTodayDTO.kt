package com.example.weather.today.data.model

import com.google.gson.annotations.SerializedName

data class WeatherTodayDTO(
    val current: Current,
    val hourly: Hourly,
) {
    data class Current(
        @SerializedName("time") val time: String,
        @SerializedName("temperature_2m") val temperature: Float,
        @SerializedName("relative_humidity_2m") val humidity: Int,
        @SerializedName("rain") val rain: Float,
        @SerializedName("wind_speed_10m") val wind: Float,
        @SerializedName("weather_code") val weather: Int,
    )

    data class Hourly(
        @SerializedName("time") val time: List<String>,
        @SerializedName("temperature_2m") val temperature: List<Float>,
        @SerializedName("weather_code") val weatherCode: List<Int>
    )
}