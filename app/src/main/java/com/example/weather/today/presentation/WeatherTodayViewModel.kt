package com.example.weather.today.presentation

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weather.WeatherApplication
import com.example.weather.common.getDay
import com.example.weather.common.getHour
import com.example.weather.today.data.WeatherTodayRepository
import com.example.weather.today.data.model.HourlyWeatherUiData
import com.example.weather.today.data.model.Weather
import com.example.weather.today.presentation.ui.WeatherTodayUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class WeatherTodayViewModel(
    private val weatherTodayRepository: WeatherTodayRepository
) : ViewModel() {
    private val _uiWeatherToday = MutableStateFlow<WeatherTodayUiState>(WeatherTodayUiState())
    val uiWeatherToday: StateFlow<WeatherTodayUiState> = _uiWeatherToday

    init {
        fetchWeatherTodayData(selectedDay = 2)
    }

    fun fetchWeatherTodayData(
        selectedDay: Int
    ) {
        _uiWeatherToday.value = WeatherTodayUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                weatherTodayRepository.getWeatherToday(-23.78f, -46.69f, forecastDays = selectedDay)
            if (result.isSuccess) {
                if (result.getOrNull() != null) {
                    val weatherToday = result.getOrNull()!!
                    _uiWeatherToday.value = WeatherTodayUiState(
                        hourlyWeather = getWeatherUiToday(
                            weather = weatherToday,
                            days = selectedDay
                        ), isLoading = false, isError = false, errorMessage = ""
                    )
                } else {
                    Log.d("WeatherTodayViewModel", "Requisition error :: Empty response")
                    _uiWeatherToday.value = WeatherTodayUiState(
                        hourlyWeather = emptyList(),
                        isLoading = false,
                        isError = true,
                        errorMessage = result.exceptionOrNull()?.message ?: "Empty server result"
                    )
                }
            } else {
                Log.d("WeatherTodayViewModel", "Requisition error :: Empty response")
                _uiWeatherToday.value = WeatherTodayUiState(
                    hourlyWeather = emptyList(),
                    isLoading = false,
                    isError = true,
                    errorMessage = result.exceptionOrNull()?.message ?: "Unknown Error"
                )
            }
        }
    }

    private fun getWeatherUiToday(weather: Weather, days: Int = 1): List<HourlyWeatherUiData> {
        val weatherHourly = weather.hourly
        val zoneId = ZoneId.of("America/Sao_Paulo")
        val now = ZonedDateTime.now(zoneId).toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val formattedDate = now.format(formatter)
        val hourlyMap: List<HourlyWeatherUiData>
        if (days == 1) {
            hourlyMap = weatherHourly.time.indices.mapNotNull { index ->
                if (getHour(weatherHourly.time[index]) >= getHour(formattedDate) && getDay(
                        weatherHourly.time[index]
                    ) == getDay(formattedDate)
                ) {
                    HourlyWeatherUiData(
                        time = weatherHourly.time[index],
                        temperature = weatherHourly.temperature[index],
                        weatherCode = weatherHourly.weatherCode[index]
                    )
                } else {
                    null
                }
            }
        } else {
            hourlyMap = weatherHourly.time.indices.mapNotNull { index ->
                if (getDay(weatherHourly.time[index]) == getDay(formattedDate) + days - 1) {
                    HourlyWeatherUiData(
                        time = weatherHourly.time[index],
                        temperature = weatherHourly.temperature[index],
                        weatherCode = weatherHourly.weatherCode[index]
                    )
                } else {
                    null
                }
            }
        }
        return hourlyMap
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return WeatherTodayViewModel(weatherTodayRepository = (application as WeatherApplication).repository) as T
            }
        }
    }
}