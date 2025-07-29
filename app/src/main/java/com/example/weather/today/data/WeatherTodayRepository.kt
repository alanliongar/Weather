package com.example.weather.today.data

import com.example.weather.today.data.local.WeatherTodayLocalDataSource
import com.example.weather.today.data.remote.WeatherTodayRemoteDataSource

class WeatherTodayRepository(
    private val local: WeatherTodayLocalDataSource,
    private val remote: WeatherTodayRemoteDataSource
) {

}