package com.example.weather.current.data.model

data class CurrentWeatherUiData(
    val city: String,
    val weatherCode: Int,
    val temperature: Float,
    val windSpeed: Float,
    val humidity: Int,
    val rain: Float,
    val date: String,
)