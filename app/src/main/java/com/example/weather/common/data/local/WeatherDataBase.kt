package com.example.weather.common.data.local

import androidx.room.Database

@Database([WeatherEntity::class], version = 1)
abstract class WeatherDataBase {

    abstract fun getWeatherDao(): WeatherDao
}