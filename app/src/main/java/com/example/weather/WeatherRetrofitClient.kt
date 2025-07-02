package com.example.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL: String = "https://api.open-meteo.com/v1/"

object WeatherRetrofitClient {
    val retrofitInstance: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
}

val linkHourlyApi =
    "https://api.open-meteo.com/v1/forecast?latitude=-23.78&longitude=-46.69&hourly=temperature_2m,weather_code&current=wind_speed_10m&forecast_days=2"
val currentWeatherApi =
    "https://api.open-meteo.com/v1/forecast?latitude=-23.78&longitude=-46.69&current=temperature_2m,relative_humidity_2m,rain,wind_speed_10m,weather_code"
val linkDailyApi =
    "https://api.open-meteo.com/v1/forecast?latitude=-23.78&longitude=-46.69&daily=weather_code,temperature_2m_max,temperature_2m_min&current=wind_speed_10m"
