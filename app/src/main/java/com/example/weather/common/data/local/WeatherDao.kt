package com.example.weather.common.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weatherEntity: WeatherEntity)

    @Query("Select * From weatherentity")
    suspend fun getTodayWeather(): WeatherEntity?

    @Query("DELETE FROM weatherentity")
    suspend fun deleteAll()
}