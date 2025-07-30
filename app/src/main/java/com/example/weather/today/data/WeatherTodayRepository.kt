package com.example.weather.today.data

import com.example.weather.today.data.local.WeatherTodayLocalDataSource
import com.example.weather.today.data.model.Weather
import com.example.weather.today.data.remote.WeatherTodayRemoteDataSource

class WeatherTodayRepository(
    private val local: WeatherTodayLocalDataSource,
    private val remote: WeatherTodayRemoteDataSource
) {

    suspend fun getWeatherToday(
        latitude: Float,
        longitude: Float,
        forecastDays: Int = 2
    ): Result<Weather?> {
        try {
            val remoteData = remote.getWeatherToday(
                latitude = latitude,
                longitude = longitude,
                forecastDays = forecastDays
            )
            val localData = local.getWeatherToday()

            if (remoteData.isSuccess) {
                val weather = remoteData.getOrNull()!!
                local.replaceUpdatedData(weather)
                return@getWeatherToday Result.success(local.getWeatherToday())
            } else {
                if (localData != null) {
                    return@getWeatherToday Result.success(localData)
                } else {
                    return@getWeatherToday Result.failure(Exception("Empty local data and error on internet request"))
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return@getWeatherToday Result.failure(ex)

        }
    }
}

