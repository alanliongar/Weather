package com.example.weather.today.data

import com.example.weather.today.data.local.WeatherTodayLocalDataSource
import com.example.weather.today.data.model.WeatherTodayDTO
import com.example.weather.today.data.remote.WeatherTodayRemoteDataSource

class WeatherTodayRepository(
    private val local: WeatherTodayLocalDataSource,
    private val remote: WeatherTodayRemoteDataSource
) {

    suspend fun getWeatherToday(
        latitude: Float,
        longitude: Float,
        forecastDays: Int = 2
    ): Result<WeatherTodayDTO?> {
        return remote.getWeatherToday(
            latitude = latitude,
            longitude = longitude,
            forecastDays = forecastDays
        )
    }
}