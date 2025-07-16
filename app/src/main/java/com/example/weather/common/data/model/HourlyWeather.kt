package com.example.weather.common.data.model

data class HourlyWeather(
    val time: String,
    val temperature: Float,
    val weatherCode: Int,
)