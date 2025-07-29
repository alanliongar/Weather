package com.example.weather.today.data.local

import com.example.weather.common.data.local.WeatherDao
import com.example.weather.common.data.local.WeatherEntity
import com.example.weather.today.data.model.HourlyWeatherUiData
import com.example.weather.today.data.model.Weather

class WeatherTodayLocalDataSource(
    private val dao: WeatherDao
) {
    suspend fun getWeatherUiToday(): List<HourlyWeatherUiData> {
        val data = getWeather()
        return data.hourly.weatherCode.indices.map { index ->
            HourlyWeatherUiData(
                data.hourly.time[index],
                data.hourly.temperature[index],
                data.hourly.weatherCode[index]
            )
        }
    }


    suspend fun getWeather(): Weather {
        val data = dao.getTodayWeather()
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

    }

    suspend fun deleteAllData() {
        dao.deleteAll()
    }

    suspend fun replaceUpdatedData(weatherEntity: WeatherEntity) {
        dao.deleteAll()
        dao.insertWeather(weatherEntity = weatherEntity)
    }


}