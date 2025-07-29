package com.example.weather.today.presentation.ui

import com.example.weather.today.data.model.HourlyWeatherUiData

data class WeatherTodayUiState(
    var hourlyWeather: List<HourlyWeatherUiData> = emptyList(),
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String = ""
)