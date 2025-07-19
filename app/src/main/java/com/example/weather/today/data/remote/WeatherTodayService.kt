package com.example.weather.today.data.remote

import com.example.weather.today.data.model.WeatherTodayDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherTodayService {

    @GET("forecast")
    fun getTodayWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("timezone") timezone: String = "GMT-3",
        @Query("forecast_days") forecastDays: Int = 1,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,rain,wind_speed_10m,weather_code",
        @Query("hourly") hourly: String = "temperature_2m,weather_code",
    ): Call<WeatherTodayDTO>
}
//https://api.open-meteo.com/v1/forecast?latitude=-23.78&longitude=-46.69&hourly=temperature_2m,weather_code&current=temperature_2m,relative_humidity_2m,rain,wind_speed_10m,weather_code&forecast_days=1&timezone=GMT-3