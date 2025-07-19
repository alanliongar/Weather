package com.example.weather.nextdays.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weather.common.convertWeatherNextDaysDTOToListDailyWeather
import com.example.weather.nextdays.data.model.WeatherNextDaysDTO
import com.example.weather.nextdays.data.remote.WeatherNextDaysService
import com.example.weather.nextdays.presentation.ui.WeatherNextDaysUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WeatherNextDaysViewModel(
    weatherNextDaysService: WeatherNextDaysService
) : ViewModel() {

    private val _uiWeatherNextDaysUiState = MutableStateFlow<WeatherNextDaysUiState>(
        WeatherNextDaysUiState()
    )

    val uiWeatherNextDaysUiState: StateFlow<WeatherNextDaysUiState> = _uiWeatherNextDaysUiState

    init {
        _uiWeatherNextDaysUiState.value = _uiWeatherNextDaysUiState.value.copy(isLoading = true)
        val callWeatherNextDaysService = weatherNextDaysService.getNextDaysWeather(-23.78f, -46.69f)
        callWeatherNextDaysService.enqueue(object : Callback<WeatherNextDaysDTO> {
            override fun onResponse(
                call: Call<WeatherNextDaysDTO?>, response: Response<WeatherNextDaysDTO?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val nextSevenDaysWeather: WeatherNextDaysDTO
                        nextSevenDaysWeather = response.body()!!
                        _uiWeatherNextDaysUiState.value =
                            _uiWeatherNextDaysUiState.value.copy(
                                nextDays = convertWeatherNextDaysDTOToListDailyWeather(response.body()!!),
                                isLoading = false
                            )
                    } else {
                        Log.d("WeatherNextDaysViewModel", "Request Error :: Empty response")
                        _uiWeatherNextDaysUiState.value =
                            _uiWeatherNextDaysUiState.value.copy(
                                isError = true,
                                errorMessage = "Request Error :: Empty response"
                            )
                    }
                } else {
                    Log.d("WeatherNextDaysViewModel", "Request Error :: ${response.errorBody()}")
                    _uiWeatherNextDaysUiState.value =
                        _uiWeatherNextDaysUiState.value.copy(
                            isError = true,
                            errorMessage = "Request Error :: ${response.errorBody()}"
                        )
                }
            }

            override fun onFailure(
                call: Call<WeatherNextDaysDTO?>, t: Throwable
            ) {
                Log.d("WeatherNextDaysViewModel", "Network Error :: ${t.message}")
                _uiWeatherNextDaysUiState.value =
                    _uiWeatherNextDaysUiState.value.copy(
                        isError = true,
                        errorMessage = "Network Error :: ${t.message}"
                    )
            }
        })
    }


    companion object {

    }
}