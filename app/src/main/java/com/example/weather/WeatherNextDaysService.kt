package com.example.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherNextDaysService {

    @GET("forecast")
    fun getNextDaysWeather(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("timezone") timezone: String = "GMT-3",
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("daily") daily: String = "weather_code,temperature_2m_max,temperature_2m_min",
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,rain,wind_speed_10m,weather_code",
    ): Call<WeatherNextDaysDTO>

    @GET("forecast")
    suspend fun getCoordinates(
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
    ): RightCoordinatesDTO
}