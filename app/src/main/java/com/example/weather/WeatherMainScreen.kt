package com.example.weather

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun WeatherMainScreen(modifier: Modifier = Modifier) {
    var selectedDay by remember { mutableStateOf<Int>(0) }
    var todayWeather by remember { mutableStateOf<WeatherTodayDTO?>(null) }
    var nextSevenDaysWeather by remember { mutableStateOf<WeatherNextDaysDTO?>(null) }

    var newHourlyMap by remember {
        mutableStateOf<List<HourlyWeather>>(emptyList())
    }

    var newDailyWeather by remember {
        mutableStateOf<List<DailyWeather>>(emptyList())
    }

    val weatherTodayService =
        WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)
    val weatherNextDaysService = WeatherRetrofitClient.retrofitInstance.create(
        WeatherNextDaysService::class.java
    )

    val callWeatherNextDaysService = weatherNextDaysService.getNextDaysWeather(-23.78f, -46.69f)
    callWeatherNextDaysService.enqueue(object : Callback<WeatherNextDaysDTO> {
        override fun onResponse(
            call: Call<WeatherNextDaysDTO?>, response: Response<WeatherNextDaysDTO?>
        ) {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    nextSevenDaysWeather = response.body()
                    if (selectedDay > 2 && nextSevenDaysWeather != null) {
                        newDailyWeather =
                            convertWeatherNextDaysDTOToListDailyWeather(nextSevenDaysWeather!!)
                    }
                } else {
                    Log.d("MainActivity", "Request Error :: Empty response")
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(
            call: Call<WeatherNextDaysDTO?>, t: Throwable
        ) {
            Log.d("MainActivity", "Network Error :: ${t.message}")
        }
    })

    val callTodayWeather = weatherTodayService.getTodayWeather(-23.78f, -46.69f)
    callTodayWeather.enqueue(object : Callback<WeatherTodayDTO> {
        override fun onResponse(
            call: Call<WeatherTodayDTO>, response: Response<WeatherTodayDTO>
        ) {
            if (response.isSuccessful) {
                val weather = response.body()
                if (weather != null) {
                    todayWeather = weather
                    newHourlyMap = convertWeatherTodayDTOToListHourlyWeather(weather, selectedDay)
                } else {
                    Log.d("MainActivity", "Request Error :: Empty response")
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<WeatherTodayDTO>, t: Throwable) {
            Log.d("MainActivity", "Network Error :: ${t.message}")
        }
    })
    LaunchedEffect(selectedDay) {
        if (todayWeather != null) {
            newHourlyMap = convertWeatherTodayDTOToListHourlyWeather(todayWeather!!, selectedDay)
        }
        if (selectedDay > 2 && nextSevenDaysWeather != null) {
            newDailyWeather = convertWeatherNextDaysDTOToListDailyWeather(nextSevenDaysWeather!!)
        }
    }

    Column() {
        Spacer(modifier = Modifier.size(28.dp))
        if (todayWeather != null) {
            Column() {
                WeatherMainContent(
                    modifier = Modifier,
                    convertWTDTOToCWInfo(todayWeather),
                    weatherTodayDTO = newHourlyMap,
                ) { days ->
                    selectedDay = days
                }
                if (selectedDay < 2) {
                    MapLibreMapView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .padding(16.dp),
                        apiKey = apiKey
                    )
                } else {
                    WeatherRowScreen(newDailyWeather = newDailyWeather)
                }
            }
        } else {
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                modifier = Modifier.padding(start = 15.dp),
                text = "Loading....",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun WeatherMainContent(
    modifier: Modifier = Modifier,
    cityCurrentWeatherInfo: CurrentWeatherInfo,
    weatherTodayDTO: List<HourlyWeather>,
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
                    text = cityCurrentWeatherInfo.city
                )
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = formatToFullDate(cityCurrentWeatherInfo.date)
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
                    text = cityCurrentWeatherInfo.temperature.toString() + "°", fontSize = 60.sp
                )
                Text(
                    text = getWeatherDescription(cityCurrentWeatherInfo.weatherCode),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = getWeatherEmoji(weatherCode = cityCurrentWeatherInfo.weatherCode),
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
                    text = cityCurrentWeatherInfo.windSpeed.toString() + " m/s",
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
                    text = cityCurrentWeatherInfo.humidity.toString() + "%",
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
                    text = String.format("%.0f", cityCurrentWeatherInfo.rain * 100) + "%",
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
                    onClick.invoke(0)
                }, text = "Today", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.size(18.dp))
            Text(
                modifier = Modifier.clickable {
                    onClick.invoke(1)
                }, text = "Tomorrow", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.size(18.dp))
            Text(
                modifier = Modifier.clickable {
                    onClick.invoke(7)
                }, text = "Next 7 days", fontSize = 18.sp, fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.size(6.dp))
        WeatherTodayScreen(weatherTodayDTO = weatherTodayDTO)
    }
}

@Preview(showBackground = true)
@Composable
private fun ClimatePreview() {
    val stuttgart: CurrentWeatherInfo = CurrentWeatherInfo(
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
        var hourlyWeather: List<HourlyWeather>
        runBlocking {
            hourlyWeather = convertWeatherTodayDTOToListHourlyWeather(
                weatherSaoPauloToday
            )
            delay(500)
        }
        WeatherMainContent(
            modifier = Modifier,
            stuttgart,
            weatherTodayDTO = hourlyWeather
        ) { it ->
            println(it)
        }
    }
}