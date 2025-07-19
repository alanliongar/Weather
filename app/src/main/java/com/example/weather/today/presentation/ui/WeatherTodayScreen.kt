package com.example.weather.today.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.common.formatToHourPeriod
import com.example.weather.common.getWeatherEmoji
import com.example.weather.today.data.model.WeatherTodayDTO
import com.example.weather.today.presentation.WeatherTodayViewModel
import com.example.weather.ui.theme.WeatherTheme
import java.util.Locale


@Composable
fun WeatherTodayScreen(
    modifier: Modifier = Modifier,
    weatherTodayViewModel: WeatherTodayViewModel
) {
    val weatherTodayUiState by weatherTodayViewModel.uiWeatherToday.collectAsState()
    WeatherTodayContent(weatherTodayUiState = weatherTodayUiState)
}

@Composable
private fun WeatherTodayContent(
    modifier: Modifier = Modifier,
    weatherTodayUiState: WeatherTodayUiState,
) {
    if (weatherTodayUiState.isLoading) {
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "Loading....",
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Green,
        )
    } else if (weatherTodayUiState.isError) {
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = weatherTodayUiState.errorMessage,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Red,
        )
    } else {
        LazyRow {
            items(weatherTodayUiState.hourlyWeather) { dayWeather ->
                WeatherHourCard(
                    time = dayWeather.time,
                    weatherCode = dayWeather.weatherCode,
                    temperature = dayWeather.temperature,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}


@Composable
private fun WeatherHourCard(
    time: String, weatherCode: Int, temperature: Float, modifier: Modifier = Modifier
) {
    Card(modifier = modifier.size(120.dp)) {
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .padding(top = 8.dp),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            text = formatToHourPeriod(time),
        )
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            text = getWeatherEmoji(weatherCode),
        )
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .padding(top = 8.dp),
            text = String.format(Locale.US, "%.1f", temperature) + "°",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ClimateHourCardPreview(modifier: Modifier = Modifier) {
    val weatherSaoPauloToday = WeatherTodayDTO(
        hourly = WeatherTodayDTO.Hourly(
            time = listOf(
                "2025-07-03T00:00",
                "2025-07-03T01:00",
                "2025-07-03T02:00",
                "2025-07-03T03:00",
                "2025-07-03T04:00",
                "2025-07-03T05:00",
                "2025-07-03T06:00",
                "2025-07-03T07:00",
                "2025-07-03T08:00",
                "2025-07-03T09:00",
                "2025-07-03T10:00",
                "2025-07-03T11:00",
                "2025-07-03T12:00",
                "2025-07-03T13:00",
                "2025-07-03T14:00",
                "2025-07-03T15:00",
                "2025-07-03T16:00",
                "2025-07-03T17:00",
                "2025-07-03T18:00",
                "2025-07-03T19:00",
                "2025-07-03T20:00",
                "2025-07-03T21:00",
                "2025-07-03T22:00",
                "2025-07-03T23:00",
                "2025-07-04T00:00",
                "2025-07-04T01:00",
                "2025-07-04T02:00",
                "2025-07-04T03:00",
                "2025-07-04T04:00",
                "2025-07-04T05:00",
                "2025-07-04T06:00",
                "2025-07-04T07:00",
                "2025-07-04T08:00",
                "2025-07-04T09:00",
                "2025-07-04T10:00",
                "2025-07-04T11:00",
                "2025-07-04T12:00",
                "2025-07-04T13:00",
                "2025-07-04T14:00",
                "2025-07-04T15:00",
                "2025-07-04T16:00",
                "2025-07-04T17:00",
                "2025-07-04T18:00",
                "2025-07-04T19:00",
                "2025-07-04T20:00",
                "2025-07-04T21:00",
                "2025-07-04T22:00",
                "2025-07-04T23:00",
            ),
            temperature = listOf(
                9.7f,
                9.6f,
                9.4f,
                9.3f,
                9.3f,
                9.4f,
                9.6f,
                9.8f,
                9.7f,
                9.5f,
                9.5f,
                9.5f,
                9.6f,
                9.8f,
                10.2f,
                11.1f,
                11.2f,
                11.1f,
                10.9f,
                10.9f,
                10.6f,
                10.3f,
                10.0f,
                9.8f,
                9.9f,
                10.0f,
                10.1f,
                10.1f,
                10.2f,
                10.2f,
                10.2f,
                10.0f,
                10.0f,
                10.0f,
                10.0f,
                10.7f,
                11.9f,
                13.5f,
                14.8f,
                15.7f,
                16.0f,
                15.9f,
                15.3f,
                14.4f,
                13.1f,
                11.7f,
                11.2f,
                10.7f
            ),
            weatherCode = listOf(
                3,
                51,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                3,
                2,
                2,
                2,
                2,
                2,
                2,
                2,
                2,
                2,
                2,
                2,
                2
            )
        ),
        current = WeatherTodayDTO.Current(
            temperature = 18f,
            humidity = 98,
            wind = 10f,
            rain = 1.0f,
            weather = 2,
            time = "2021-09-12T10:00"
        )
    )
    WeatherTheme {
        WeatherHourCard(
            modifier = modifier.size(120.dp),
            time = weatherSaoPauloToday.current.time,
            weatherCode = weatherSaoPauloToday.current.weather,
            temperature = 16f
        )
    }
}