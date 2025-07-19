package com.example.weather.today.presentation

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weather.common.convertWeatherHourlyFromDTOToListHourlyWeather
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.today.WeatherTodayRepository
import com.example.weather.today.data.remote.WeatherTodayService
import com.example.weather.today.presentation.ui.WeatherTodayUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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
                        hourlyWeather = convertWeatherHourlyFromDTOToListHourlyWeather(
                            weatherToday.hourly, days = selectedDay
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val weatherTodayService =
                    WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)
                val weatherTodayRepository: WeatherTodayRepository =
                    WeatherTodayRepository(weatherTodayService)
                return WeatherTodayViewModel(weatherTodayRepository) as T
            }
        }
    }
}