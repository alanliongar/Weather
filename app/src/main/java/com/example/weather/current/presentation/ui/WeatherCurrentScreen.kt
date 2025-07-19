package com.example.weather.current.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.nextdays.presentation.ui.WeatherNextDaysRowsScreen
import com.example.weather.today.data.model.WeatherTodayDTO
import com.example.weather.today.presentation.ui.WeatherTodayScreen
import com.example.weather.apiKey
import com.example.weather.common.presentation.ui.MapLibreMapView
import com.example.weather.common.formatToFullDate
import com.example.weather.common.getWeatherDescription
import com.example.weather.common.getWeatherEmoji
import com.example.weather.common.data.remote.WeatherRetrofitClient
import com.example.weather.current.data.model.CurrentWeatherUiData
import com.example.weather.current.presentation.WeatherCurrentViewModel
import com.example.weather.nextdays.presentation.WeatherNextDaysViewModel
import com.example.weather.today.data.remote.WeatherTodayService
import com.example.weather.today.presentation.WeatherTodayViewModel
import com.example.weather.ui.theme.WeatherTheme

@Composable
fun WeatherCurrentScreen(
    modifier: Modifier = Modifier,
    weatherCurrentViewModel: WeatherCurrentViewModel,
    weatherTodayViewModel: WeatherTodayViewModel,
    weatherNextDaysViewModel: WeatherNextDaysViewModel
) {
    val selectedDay by weatherCurrentViewModel.selectedDays.collectAsState()
    val currentWeather by weatherCurrentViewModel.uiCurrentWeather.collectAsState()

    LaunchedEffect(selectedDay) {
        if (selectedDay < 7) {
            weatherTodayViewModel.updateUiWeatherToday(selectedDay)
        }
    }

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.size(28.dp))
        if (currentWeather.isLoading) {
            Spacer(
                modifier = Modifier.size(8.dp)
            )
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "Loading....",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )
        } else if (currentWeather.isError) {
            Spacer(
                modifier = Modifier.size(8.dp)
            )
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = currentWeather.errorMessage,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )
        } else {
            Column() {
                WeatherMainContent(
                    modifier = Modifier,
                    currentWeather.currentWeatherUiData,
                    selectedDays = selectedDay,
                    weatherTodayViewModel = weatherTodayViewModel,
                ) { days ->
                    weatherCurrentViewModel.changeDays(selectedDays = days)
                }
                if (selectedDay < 3) {
                    MapLibreMapView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .padding(16.dp),
                        apiKey = apiKey
                    )
                } else {
                    WeatherNextDaysRowsScreen(
                        weatherNextDaysViewModel = weatherNextDaysViewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherMainContent(
    modifier: Modifier = Modifier,
    cityCurrentWeatherUiData: CurrentWeatherUiData,
    weatherTodayViewModel: WeatherTodayViewModel,
    selectedDays: Int,
    onClick: (Int) -> Unit,
) {
    Column(modifier = modifier.padding(10.dp)) {
        Row(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = cityCurrentWeatherUiData.city
                )
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = formatToFullDate(cityCurrentWeatherUiData.date)
                )
            }
        }
        //line of temperature, status and image
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = cityCurrentWeatherUiData.temperature.toString() + "°", fontSize = 60.sp
                )
                Text(
                    text = getWeatherDescription(cityCurrentWeatherUiData.weatherCode),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = getWeatherEmoji(weatherCode = cityCurrentWeatherUiData.weatherCode),
                fontSize = 90.sp,
                modifier = modifier,
                textAlign = TextAlign.Center
            )

        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = "\uD83D\uDCA8", //Wind emoji
                    fontSize = 32.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = cityCurrentWeatherUiData.windSpeed.toString() + " m/s",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Wind",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = "\uD83D\uDCA6", //Water emoji
                    fontSize = 32.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = cityCurrentWeatherUiData.humidity.toString() + "%",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Humidity",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = "☔", //Rain emoji
                    fontSize = 32.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = String.format("%.0f", cityCurrentWeatherUiData.rain * 100) + "%",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Rain",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.size(14.dp))
        Row(modifier = modifier) {
            Text(
                modifier = Modifier.clickable {
                    onClick.invoke(1)
                }, text = "Today", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.size(18.dp))
            Text(
                modifier = Modifier.clickable {
                    onClick.invoke(2)
                }, text = "Tomorrow", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.size(18.dp))
            Text(
                modifier = Modifier.clickable {
                    onClick.invoke(7)
                }, text = "Next 7 days", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
        }
        if (selectedDays < 7) {
            Spacer(modifier = Modifier.size(6.dp))
            WeatherTodayScreen(
                weatherTodayViewModel = weatherTodayViewModel
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClimatePreview() {
    val stuttgart: CurrentWeatherUiData = CurrentWeatherUiData(
        "Sao Paulo", 2, 18f, 10f, 98, 1.0f, "2021-09-12T10:00"
    )
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
        val weatherTodayService: WeatherTodayService =
            WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)

        val weatherTodayViewModel = WeatherTodayViewModel(weatherTodayService)


        WeatherMainContent(
            modifier = Modifier,
            stuttgart,
            selectedDays = 1,
            weatherTodayViewModel = weatherTodayViewModel
        ) { it ->
            println(it)
        }
    }
}