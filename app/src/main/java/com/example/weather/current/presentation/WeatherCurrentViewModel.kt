package com.example.weather.current.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.current.data.model.CurrentWeatherUiData
import com.example.weather.current.data.remote.WeatherCurrentService
import com.example.weather.current.presentation.ui.CurrentWeatherUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherCurrentViewModel(
    private val weatherCurrentService: WeatherCurrentService,
) : ViewModel() {

    private val _selectedDays = MutableStateFlow<Int>(1)
    val selectedDays: StateFlow<Int> = _selectedDays

    fun changeDays(selectedDays: Int) {
        _selectedDays.value = selectedDays
    }

    private val _uiCurrentWeather = MutableStateFlow<CurrentWeatherUiState>(CurrentWeatherUiState())
    val uiCurrentWeather: StateFlow<CurrentWeatherUiState> = _uiCurrentWeather

    init {
        fetchCurrentWeatherData()
    }

    private fun fetchCurrentWeatherData() {
        _uiCurrentWeather.value = _uiCurrentWeather.value.copy(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = weatherCurrentService.getCurrentWeatherData(
                    -23.78f,
                    -46.69f,
                    forecastDays = _selectedDays.value
                )
                val weather = response.body()
                if (weather != null) {
                    _uiCurrentWeather.value = _uiCurrentWeather.value.copy(
                        currentWeatherUiData =
                            CurrentWeatherUiData(
                                city = "Sao Paulo",
                                weatherCode = weather.current.weather,
                                temperature = weather.current.temperature,
                                windSpeed = weather.current.temperature,
                                humidity = weather.current.humidity,
                                rain = weather.current.rain,
                                date = weather.current.time
                            ),
                        isLoading = false,
                        isError = false,
                        errorMessage = "No error message"
                    )
                } else {
                    Log.d("WeatherCurrentViewModel", "Request Error :: Empty response")
                    _uiCurrentWeather.value =
                        CurrentWeatherUiState(
                            isError = true,
                            errorMessage = "Request Error :: Empty response"
                        )
                }
            } catch (ex: Exception) {
                Log.d("WeatherCurrentViewModel", "Network Error :: ${ex.message}")
                _uiCurrentWeather.value =
                    CurrentWeatherUiState(
                        isError = true,
                        errorMessage = "Network Error :: ${ex.message}"
                    )
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val weatherCurrentService =
                    WeatherRetrofitClient.retrofitInstance.create(WeatherCurrentService::class.java)
                return WeatherCurrentViewModel(weatherCurrentService = weatherCurrentService) as T
            }
        }
    }
}