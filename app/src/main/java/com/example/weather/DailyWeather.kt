package com.example.weather

data class DailyWeather(
    val time: String,
    val temperatureMin: Int,
    val temperatureMax: Int,
    val weatherCode: Int
)
