package com.example.weather.nextdays.data.model

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