package com.example.weather

import android.app.Application
import androidx.room.Room
import com.example.weather.common.data.local.WeatherDataBase

class WeatherApplication : Application() {
    val db by lazy {
        Room.databaseBuilder(applicationContext, WeatherDataBase::class.java, "database-weather").build()
    }
}