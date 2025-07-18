package com.example.weather.nextdays.presentation.ui

import com.example.weather.nextdays.data.model.DailyWeather

data class WeatherNextDaysUiState(
    var nextDays: List<DailyWeather> = emptyList(),
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String = ""
)