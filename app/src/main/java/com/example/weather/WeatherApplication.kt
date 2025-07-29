package com.example.weather

import android.app.Application
import androidx.room.Room
import com.example.weather.common.data.local.WeatherDataBase
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.today.data.WeatherTodayRepository
import com.example.weather.today.data.local.WeatherTodayLocalDataSource
import com.example.weather.today.data.remote.WeatherTodayRemoteDataSource
import com.example.weather.today.data.remote.WeatherTodayService

class WeatherApplication : Application() {
    private val db: WeatherDataBase by lazy {
        Room.databaseBuilder(applicationContext, WeatherDataBase::class.java, "database-weather")
            .build()
    }

    private val service: WeatherTodayService by lazy {
        WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)
    }

    private val remoteDataSource: WeatherTodayRemoteDataSource by lazy{
        WeatherTodayRemoteDataSource(service)
    }

    private val localDataSource: WeatherTodayLocalDataSource by lazy{
        WeatherTodayLocalDataSource(db.getWeatherDao())
    }

    val repository: WeatherTodayRepository by lazy {
        WeatherTodayRepository(local = localDataSource, remote = remoteDataSource)
    }
}