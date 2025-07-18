package com.example.weather.current.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weather.current.data.model.CurrentWeatherUiData
import com.example.weather.current.presentation.ui.CurrentWeatherUiState
import com.example.weather.today.data.model.WeatherTodayDTO
import com.example.weather.today.data.remote.WeatherTodayService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherCurrentViewModel(
    private val weatherTodayService: WeatherTodayService
) : ViewModel() {

    private val _uiCurrentWeather = MutableStateFlow<CurrentWeatherUiState>(CurrentWeatherUiState())
    val uiCurrentWeather: StateFlow<CurrentWeatherUiState> = _uiCurrentWeather

    private val _uiHourlyWeather = MutableStateFlow<WeatherTodayDTO.Hourly>(
        WeatherTodayDTO.Hourly(
            emptyList(),
            emptyList(),
            emptyList()
        )
    )
    val uiHourlyWeather: StateFlow<WeatherTodayDTO.Hourly> = _uiHourlyWeather

    init {
        _uiCurrentWeather.value = _uiCurrentWeather.value.copy(
            isLoading = true
        )
        val callTodayWeather = weatherTodayService.getTodayWeather(-23.78f, -46.69f)
        callTodayWeather.enqueue(object : Callback<WeatherTodayDTO> {
            override fun onResponse(
                call: Call<WeatherTodayDTO>, response: Response<WeatherTodayDTO>
            ) {
                if (response.isSuccessful) {
                    val weather = response.body()
                    if (weather != null) {
                        _uiHourlyWeather.value = weather.hourly
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
                        /*newHourlyMap = convertWeatherTodayDTOToListHourlyWeather(weather, selectedDay)*/
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
}