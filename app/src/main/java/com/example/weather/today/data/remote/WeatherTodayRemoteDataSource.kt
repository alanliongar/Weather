package com.example.weather.today.data.remote

import com.example.weather.today.data.model.Weather
import com.example.weather.today.data.model.WeatherTodayDTO
import com.google.gson.annotations.SerializedName

class WeatherTodayRemoteDataSource(
    private val weatherTodayService: WeatherTodayService
) {
    suspend fun getWeatherToday(
        latitude: Float,
        longitude: Float,
        forecastDays: Int = 2
    ): Result<Weather?> {
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
                    val weatherCurrent = Weather.Current(
                        time = remoteWeatherToday.current.time,
                        temperature = remoteWeatherToday.current.temperature,
                        humidity = remoteWeatherToday.current.humidity,
                        rain = remoteWeatherToday.current.rain,
                        wind = remoteWeatherToday.current.wind,
                        weather = remoteWeatherToday.current.weather
                    )
                    val weatherHourly = Weather.Hourly(
                        time = remoteWeatherToday.hourly.time,
                        temperature = remoteWeatherToday.hourly.temperature,
                        weatherCode = remoteWeatherToday.hourly.weatherCode)

                    val weather = Weather(
                        current = weatherCurrent, hourly = weatherHourly
                    )

                    Result.success(weather)
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