package com.example.weather.today.data.remote

import com.example.weather.today.data.model.WeatherTodayDTO

class WeatherTodayRemoteDataSource(
    private val weatherTodayService: WeatherTodayService
) {
    suspend fun getWeatherToday(
        latitude: Float,
        longitude: Float,
        forecastDays: Int = 2
    ): Result<WeatherTodayDTO?> {
        return try {
            val result =
                weatherTodayService.getTodayWeather(
                    latitude = latitude,
                    longitude = longitude,
                    forecastDays = forecastDays
                )
            if (result.isSuccessful) {
                val remoteWeatherToday = result.body()
                if (remoteWeatherToday != null) {
                    Result.success(remoteWeatherToday)
                } else {
                    Result.failure(Exception("Empty result from server"))
                }
            } else {
                Result.failure(Exception(result.errorBody().toString()))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}