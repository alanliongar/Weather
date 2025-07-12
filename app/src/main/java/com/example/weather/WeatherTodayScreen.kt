package com.example.weather

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.ui.theme.WeatherTheme
import java.util.Locale


@Composable
fun WeatherTodayScreen(
    modifier: Modifier = Modifier,
    weatherTodayDTO: List<HourlyWeather>
) {
    WeatherTodayContent(weatherTodayDTO = weatherTodayDTO)
    Spacer(modifier = Modifier.size(6.dp))
    MapLibreMapView(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .padding(16.dp),
        apiKey = apiKey
    )
}

@Composable
fun WeatherTodayContent(
    modifier: Modifier = Modifier,
    weatherTodayDTO: List<HourlyWeather>
    ) {
    LazyRow {
        items(weatherTodayDTO) { dayWeather ->
            WeatherHourCard(
                time = dayWeather.time,
                weatherCode = dayWeather.weatherCode,
                temperature = dayWeather.temperature,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}


@Composable
fun WeatherHourCard(
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
fun ClimateHourCardPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        WeatherHourCard(
            modifier = modifier.size(120.dp),
            time = weatherSaoPauloToday.current.time,
            weatherCode = weatherSaoPauloToday.current.weather,
            temperature = 16f
        )
    }
}