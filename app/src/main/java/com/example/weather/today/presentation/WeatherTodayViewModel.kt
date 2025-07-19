package com.example.weather.today.presentation

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weather.common.convertWeatherHourlyFromDTOToListHourlyWeather
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.today.data.model.WeatherTodayDTO
import com.example.weather.today.data.remote.WeatherTodayService
import com.example.weather.today.presentation.ui.WeatherTodayUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WeatherTodayViewModel(
    private val weatherTodayService: WeatherTodayService
) : ViewModel() {
    private val _uiWeatherToday = MutableStateFlow<WeatherTodayUiState>(WeatherTodayUiState())
    val uiWeatherToday: StateFlow<WeatherTodayUiState> = _uiWeatherToday

    init {
        updateUiWeatherToday(selectedDay = 2)
    }

    fun updateUiWeatherToday(
        selectedDay: Int
    ) {
        _uiWeatherToday.value = _uiWeatherToday.value.copy(
            hourlyWeather = emptyList(),
            isLoading = true,
            isError = false,
            errorMessage = ""
        )
        val callWeatherToday =
            weatherTodayService.getTodayWeather(-23.78f, -46.69f, forecastDays = 2)
        callWeatherToday.enqueue(object : Callback<WeatherTodayDTO> {
            override fun onResponse(
                call: Call<WeatherTodayDTO?>, response: Response<WeatherTodayDTO?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val weatherToday = response.body()!!
                        _uiWeatherToday.value = _uiWeatherToday.value.copy(
                            hourlyWeather = convertWeatherHourlyFromDTOToListHourlyWeather(
                                weatherToday.hourly, days = selectedDay
                            ), isLoading = false, isError = false, errorMessage = ""
                        )
                        val pagodin = convertWeatherHourlyFromDTOToListHourlyWeather(
                            weatherToday.hourly, days = 1
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


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val weatherTodayService =
                    WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)
                return WeatherTodayViewModel(weatherTodayService) as T
            }
        }
    }
}