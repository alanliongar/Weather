package com.example.weather.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([WeatherEntity::class], version = 1)
abstract class WeatherDataBase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao
}