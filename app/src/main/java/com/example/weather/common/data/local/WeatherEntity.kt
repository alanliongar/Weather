package com.example.weather.common.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class WeatherEntity(
    @PrimaryKey val id: Int,
    val current: Current,
    val hourly: Hourly,
) {
    @Serializable
    data class Current(
        val time: String,
        val temperature: Float,
        val humidity: Int,
        val rain: Float,
        val wind: Float,
        val weather: Int,
    )
    @Serializable
    data class Hourly(
        val time: List<String>,
        val temperature: List<Float>,
        val weatherCode: List<Int>
    )
}
