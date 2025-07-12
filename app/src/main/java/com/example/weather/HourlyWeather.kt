package com.example.weather

data class HourlyWeather(
    val time: String,
    val temperature: Float,
    val weatherCode: Int,
)