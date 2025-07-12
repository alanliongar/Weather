package com.example.weather

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    var selectedDay by remember { mutableStateOf(0) }
    var todayWeather by remember { mutableStateOf<WeatherTodayDTO?>(null) }
    var nextDaysWeather by remember { mutableStateOf<WeatherNextDaysDTO?>(null) }

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

    val callWeatherNextDaysService =
        weatherNextDaysService.getNextDaysWeather(-23.78f, -46.69f)
    callWeatherNextDaysService.enqueue(object : Callback<WeatherNextDaysDTO> {
        override fun onResponse(
            call: Call<WeatherNextDaysDTO?>,
            response: Response<WeatherNextDaysDTO?>
        ) {
            if (response.isSuccessful) {
                if (response.body() != null) {
                    nextDaysWeather = response.body()
                } else {
                    Log.d("MainActivity", "Request Error :: Empty response")
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(
            call: Call<WeatherNextDaysDTO?>,
            t: Throwable
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
            newHourlyMap =
                convertWeatherTodayDTOToListHourlyWeather(todayWeather!!, selectedDay)
        }
        if (selectedDay > 2 && nextDaysWeather != null) {
            newDailyWeather =
                convertWeatherNextDaysDTOToListDailyWeather(nextDaysWeather!!)
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column() {
            Spacer(modifier = Modifier.size(28.dp))
            if (todayWeather != null) {
                val weatherInfo = WeatherInfo(
                    "Sao Paulo",
                    todayWeather?.current?.weather ?: 0,
                    todayWeather?.current?.temperature ?: 0f,
                    todayWeather?.current?.wind ?: 0f,
                    todayWeather?.current?.humidity ?: 0,
                    todayWeather?.current?.rain ?: 0f,
                    todayWeather?.current?.time ?: "Error"
                )
                runBlocking {
                    newHourlyMap =
                        convertWeatherTodayDTOToListHourlyWeather(
                            weatherTodayDTO = todayWeather!!,
                            days = selectedDay
                        )
                    delay(500)
                }
                Column() {
                    WeatherMainContent(
                        weatherInfo,
                        weatherTodayDTO = newHourlyMap,
                        modifier = Modifier
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
                }
            } else {
                Text("Loading....")
            }
        }
    }
}

@Composable
fun WeatherMainContent(
    cityWeatherInfo: WeatherInfo,
    modifier: Modifier = Modifier,
    weatherTodayDTO: List<HourlyWeather>,
    onClick: (Int) -> Unit
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
                    text = cityWeatherInfo.city
                )
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = formatToFullDate(cityWeatherInfo.date)
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
                    text = cityWeatherInfo.temperature.toString() + "°", fontSize = 60.sp
                )
                Text(
                    text = getWeatherDescription(cityWeatherInfo.weatherCode),
                    fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = getWeatherEmoji(weatherCode = cityWeatherInfo.weatherCode),
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
                    text = cityWeatherInfo.windSpeed.toString() + " m/s",
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
                    text = cityWeatherInfo.humidity.toString() + "%",
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
                    text = String.format("%.0f", cityWeatherInfo.rain * 100) + "%",
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
                modifier = Modifier.clickable { onClick.invoke(0) },
                text = "Today",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.size(18.dp))
            Text(
                modifier = Modifier.clickable { onClick.invoke(1) },
                text = "Tomorrow",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = modifier.size(18.dp))
            Text(
                modifier = Modifier.clickable { onClick.invoke(7) },
                text = "Next 7 days",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.size(6.dp))
        LazyRow {
            items(weatherTodayDTO) { it ->
                WeatherHourCard(
                    time = it.time,
                    weatherCode = it.weatherCode,
                    temperature = it.temperature,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClimatePreview() {
    WeatherTheme {
        var hourlyWeather: List<HourlyWeather>
        runBlocking {
            hourlyWeather = convertWeatherTodayDTOToListHourlyWeather(
                weatherSaoPauloToday
            )
            delay(500)
        }

        WeatherMainContent(
            stuttgart, weatherTodayDTO = hourlyWeather
        ) { it ->
            println(it)
        }
    }
}