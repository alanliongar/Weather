package com.example.weather.current.data.remote

import com.example.weather.today.data.model.WeatherTodayDTO
import retrofit2.Call
import retrofit2.http.GET

interface WeatherCurrentService {

    @GET("forecast")
    fun getCurrentWeatherData(): Call<WeatherTodayDTO>
}