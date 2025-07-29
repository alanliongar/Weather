package com.example.weather.today.data.local

import com.example.weather.common.data.local.WeatherDao
import com.example.weather.common.data.model.HourlyWeatherUiData

class WeatherTodayLocalDataSource(
    private val dao: WeatherDao
) {
    suspend fun getWeatherToday(): List<HourlyWeatherUiData> {
        val data = dao.getTodayWeather()
        return data.hourly.weatherCode.indices.map { index ->
            HourlyWeatherUiData(
                data.hourly.time[index],
                data.hourly.temperature[index],
                data.hourly.weatherCode[index]
            )
        }
    }

    suspend fun deleteAllData(){
        dao.deleteAll()
    }

}