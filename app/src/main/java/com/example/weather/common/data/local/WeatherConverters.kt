package com.example.weather.common.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromCurrent(current: WeatherEntity.Current): String {
        return json.encodeToString(current)
    }

    @TypeConverter
    fun toCurrent(data: String): WeatherEntity.Current {
        return json.decodeFromString(data)
    }

    @TypeConverter
    fun fromHourly(hourly: WeatherEntity.Hourly): String {
        return json.encodeToString(hourly)
    }

    @TypeConverter
    fun toHourly(data: String): WeatherEntity.Hourly {
        return json.decodeFromString(data)
    }

}