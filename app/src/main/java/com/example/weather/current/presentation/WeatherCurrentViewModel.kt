package com.example.weather.current.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.current.data.model.CurrentWeatherUiData
import com.example.weather.current.data.remote.WeatherCurrentService
import com.example.weather.current.presentation.ui.CurrentWeatherUiState
import com.example.weather.today.data.model.WeatherTodayDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        _uiCurrentWeather.value = _uiCurrentWeather.value.copy(
            isLoading = true
        )
        val callTodayWeather = weatherCurrentService.getCurrentWeatherData(
            -23.78f,
            -46.69f,
            forecastDays = _selectedDays.value
        )
        callTodayWeather.enqueue(object : Callback<WeatherTodayDTO> {
            override fun onResponse(
                call: Call<WeatherTodayDTO>, response: Response<WeatherTodayDTO>
            ) {
                if (response.isSuccessful) {
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
                } else {
                    Log.d("WeatherCurrentViewModel", "Request Error :: ${response.errorBody()}")
                    _uiCurrentWeather.value =
                        CurrentWeatherUiState(
                            isError = true,
                            errorMessage = "Request Error :: ${response.errorBody()}"
                        )
                }
            }

            override fun onFailure(call: Call<WeatherTodayDTO>, t: Throwable) {
                _uiCurrentWeather.value = CurrentWeatherUiState(
                    isError = true,
                    errorMessage = "Network Error :: ${t.message}"
                )
                Log.d("WeatherCurrentViewModel", "Network Error :: ${t.message}")
            }
        })
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