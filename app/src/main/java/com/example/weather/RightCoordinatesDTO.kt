package com.example.weather

import com.google.gson.annotations.SerializedName

data class RightCoordinatesDTO(
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
)
