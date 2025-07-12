package com.example.weather

data class WeatherInfo(
    val city: String,
    val weatherCode: Int,
    val temperature: Float,
    val windSpeed: Float,
    val humidity: Int,
    val rain: Float,
    val date: String,
)
