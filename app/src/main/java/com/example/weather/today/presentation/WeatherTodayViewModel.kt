package com.example.weather.today.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weather.common.convertWeatherHourlyFromDTOToListHourlyWeather
import com.example.weather.today.data.model.WeatherTodayDTO
import com.example.weather.today.data.remote.WeatherTodayService
import com.example.weather.today.presentation.ui.WeatherTodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherTodayViewModel(
    weatherTodayService: WeatherTodayService
) : ViewModel() {


    private val _uiWeatherToday = MutableStateFlow<WeatherTodayUiState>(WeatherTodayUiState())
    val uiWeatherToday: StateFlow<WeatherTodayUiState> = _uiWeatherToday

    init {
        _uiWeatherToday.value = _uiWeatherToday.value.copy(
            hourlyWeather = emptyList(),
            isLoading = true,
            isError = false,
            errorMessage = ""
        )
        val callWeatherToday = weatherTodayService.getTodayWeather(-23.78f, -46.69f)
        callWeatherToday.enqueue(object : Callback<WeatherTodayDTO> {
            override fun onResponse(
                call: Call<WeatherTodayDTO?>, response: Response<WeatherTodayDTO?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val weatherToday = response.body()!!
                        _uiWeatherToday.value = _uiWeatherToday.value.copy(
                            hourlyWeather = convertWeatherHourlyFromDTOToListHourlyWeather(
                                weatherToday.hourly
                            ), isLoading = false, isError = false, errorMessage = ""
                        )
                    } else {
                        Log.d("WeatherTodayViewModel", "Requisition error :: Empty response")
                        _uiWeatherToday.value = _uiWeatherToday.value.copy(
                            hourlyWeather = emptyList(),
                            isLoading = true,
                            isError = false,
                            errorMessage = ""
                        )
                    }
                } else {
                    Log.d("WeatherTodayViewModel", "Request Error :: ${response.errorBody()}")
                    _uiWeatherToday.value = _uiWeatherToday.value.copy(
                        hourlyWeather = emptyList(),
                        isLoading = false,
                        isError = true,
                        errorMessage = "Request Error :: ${response.errorBody()}"
                    )
                }

            }

            override fun onFailure(
                call: Call<WeatherTodayDTO?>, t: Throwable
            ) {
                Log.d("WeatherTodayViewModel", "Network error :: ${t.message}")
                _uiWeatherToday.value = _uiWeatherToday.value.copy(
                    hourlyWeather = emptyList(),
                    isLoading = false,
                    isError = true,
                    errorMessage = "Network error :: ${t.message}"
                )
            }
        })
    }
}