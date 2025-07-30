package com.example.weather.today.data.local

import com.example.weather.common.data.local.WeatherDao
import com.example.weather.common.data.local.WeatherEntity
import com.example.weather.today.data.model.Weather

class WeatherTodayLocalDataSource(
    private val dao: WeatherDao
) {
    suspend fun replaceUpdatedData(weather: Weather) {
        val current = weather.current
        val hourly = weather.hourly


        val weatherEntity: WeatherEntity = WeatherEntity(
            id = 1,
            current = WeatherEntity.Current(
                time = current.time,
                temperature = current.temperature,
                humidity = current.humidity,
                rain = current.rain,
                wind = current.wind,
                weather = current.weather
            ),
            hourly = WeatherEntity.Hourly(
                time = hourly.time,
                temperature = hourly.temperature,
                weatherCode = hourly.weatherCode
            )
        )
        dao.deleteAll()
        dao.insertWeather(weatherEntity = weatherEntity)
    }


    suspend fun getWeatherToday(): Weather? {
        val data = dao.getTodayWeather()
        if (data != null) {
            val weatherCurrent = Weather.Current(
                time = data.current.time,
                temperature = data.current.temperature,
                humidity = data.current.humidity,
                rain = data.current.rain,
                wind = data.current.wind,
                weather = data.current.weather
            )
            val weatherHourly = Weather.Hourly(
                time = data.hourly.time,
                temperature = data.hourly.temperature,
                weatherCode = data.hourly.weatherCode
            )
            return Weather(current = weatherCurrent, hourly = weatherHourly)
        } else {
            return null
        }
    }

    suspend fun deleteAllData() {
        dao.deleteAll()
    }


}