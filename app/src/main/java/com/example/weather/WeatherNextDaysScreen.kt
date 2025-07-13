package com.example.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun WeatherRowScreen(
    modifier: Modifier = Modifier,
    newDailyWeather: List<DailyWeather>
) {
    WeatherRowContent(newDailyWeather = newDailyWeather)
}

@Composable
private fun WeatherRowContent(
    modifier: Modifier = Modifier,
    newDailyWeather: List<DailyWeather>
) {
    LazyColumn() {
        items(newDailyWeather) { forecast ->
            WeatherRow(
                temperatureMax = forecast.temperatureMax,
                temperatureMin = forecast.temperatureMin,
                weatherCode = forecast.weatherCode,
                time = forecast.time
            )
        }
    }
}


@Composable
private fun WeatherRow(
    modifier: Modifier = Modifier.fillMaxWidth(),
    temperatureMin: Int, temperatureMax: Int, weatherCode: Int, time: String
) {
    Row(
        modifier = modifier.padding(top = 24.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = time.padEnd(10),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.size(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = String.format("%+03d", temperatureMin).replace('+', ' ') + "°",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(8.dp))
            TempBar(
                modifier = modifier,
                temperatureMin = temperatureMin,
                temperatureMax = temperatureMax,
                totalWidth = 126
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = String.format("%+03d", temperatureMax).replace('+', ' ') + "°",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.size(14.dp))
        Text(getWeatherEmoji(weatherCode = weatherCode), fontSize = 32.sp)
    }
}

@Composable
private fun TempBar(
    modifier: Modifier = Modifier,
    temperatureMin: Int,
    temperatureMax: Int,
    totalWidth: Int,
) {
    val color = getBarColor((temperatureMax + temperatureMin) / 2)
    val backgroundColor = MaterialTheme.colorScheme.background
    val boxes: Pair<Float, Float>
    boxes = getBarSize(temperatureMin, temperatureMax)
    val barHeight = 8.dp
    Row() {
        Box(
            modifier = Modifier
                .width(width = totalWidth.dp)
                .height(barHeight)
                .background(color = backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .width(width = totalWidth.dp * boxes.first)
                    .height(barHeight)
                    .background(color)
            ) {
                Box(
                    modifier = Modifier
                        .width(width = totalWidth.dp * boxes.second * boxes.first)
                        .height(barHeight)
                        .background(backgroundColor)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun weatherRowPreview(modifier: Modifier = Modifier) {
    val weatherSaoPauloNextDays = WeatherNextDaysDTO(
        current = WeatherNextDaysDTO.Current(
            temperature = 18f,
            humidity = 98,
            wind = 10f,
            rain = 1.0f,
            weather = 2,
            time = "2021-09-12T10:00"
        ),
        daily = WeatherNextDaysDTO.Daily(
            time = listOf(
                "2025-07-05",
                "2025-07-06",
                "2025-07-07",
                "2025-07-08",
                "2025-07-09",
                "2025-07-10",
                "2025-07-11"
            ),
            weatherCode = listOf(45, 45, 45, 45, 45, 45, 45),
            temperatureMax = listOf(18.5f, 16.5f, 18.3f, 20.5f, 16.4f, 18.2f, 19.9f),
            temperatureMin = listOf(7.0f, 7.5f, 8.5f, 8.5f, 5.9f, 9.8f, 8.7f)
        )
    )
    WeatherRow(temperatureMin = 13, temperatureMax = 22, weatherCode = 2, time = "Today")
}