package com.example.weather.current.data.remote

import com.example.weather.today.data.model.WeatherTodayDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherCurrentService {

    @GET("forecast")
    fun getCurrentWeatherData(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("timezone") timezone: String = "GMT-3",
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,rain,wind_speed_10m,weather_code",
        @Query("hourly") hourly: String = "temperature_2m,weather_code",
    ): Call<WeatherTodayDTO>
}