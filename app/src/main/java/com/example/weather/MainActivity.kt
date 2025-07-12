package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.weather.ui.theme.WeatherTheme
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

const val apiKey = BuildConfig.API_KEY

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MapLibre.getInstance(
            this,
            apiKey,
            WellKnownTileServer.MapLibre
        )
        setContent {
            WeatherTheme {
                WeatherMainScreen()
            }
        }
    }
}