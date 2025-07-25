package com.example.weather.common.data.local

import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database([WeatherEntity::class], version = 1)
@TypeConverters(WeatherConverters::class)
abstract class WeatherDataBase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao
}