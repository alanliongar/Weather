package com.example.weather.current.presentation.ui

import com.example.weather.current.data.model.CurrentWeatherUiData
import kotlin.String

data class CurrentWeatherUiState(
    var currentWeatherUiData: CurrentWeatherUiData = defaultCurrentWeatherUiData,
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String = "Something went wrong"
)


private val defaultCurrentWeatherUiData = CurrentWeatherUiData(
    city = "None",
    weatherCode = 1,
    temperature = 0f,
    windSpeed = 0f,
    humidity = 0,
    rain = 0f,
    date = ""
)