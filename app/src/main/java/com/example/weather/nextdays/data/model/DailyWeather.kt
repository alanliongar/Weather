package com.example.weather.nextdays.data.model

data class DailyWeather(
    val time: String,
    val temperatureMin: Int,
    val temperatureMax: Int,
    val weatherCode: Int
)