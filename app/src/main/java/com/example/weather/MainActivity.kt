package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.current.data.remote.WeatherCurrentService
import com.example.weather.current.presentation.WeatherCurrentViewModel
import com.example.weather.current.presentation.ui.WeatherCurrentScreen
import com.example.weather.nextdays.data.remote.WeatherNextDaysService
import com.example.weather.nextdays.presentation.WeatherNextDaysViewModel
import com.example.weather.today.data.remote.WeatherTodayService
import com.example.weather.today.presentation.WeatherTodayViewModel
import com.example.weather.ui.theme.WeatherTheme
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

const val apiKey = BuildConfig.API_KEY

class MainActivity : ComponentActivity() {
    val weatherTodayViewModel by viewModels<WeatherTodayViewModel> { WeatherTodayViewModel.Factory }
    val weatherCurrentViewModel by viewModels<WeatherCurrentViewModel> { WeatherCurrentViewModel.Factory }
    val weatherNextDaysViewModel by viewModels<WeatherNextDaysViewModel> { WeatherNextDaysViewModel.Factory }

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
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherCurrentScreen(
                        weatherCurrentViewModel = weatherCurrentViewModel,
                        weatherTodayViewModel = weatherTodayViewModel,
                        weatherNextDaysViewModel = weatherNextDaysViewModel
                    )
                }
            }
        }
    }
}