package com.example.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import retrofit2.*
import java.util.Locale

private const val apiKey = BuildConfig.API_KEY

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
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column() {
                        Spacer(modifier = Modifier.size(28.dp))
                        if (todayWeather != null) {
                            val climateInfo = ClimateInfo(
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
                                delay(1000)
                            }
                            Column() {
                                Climate(
                                    climateInfo,
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
                                    //Chamar as WeatherRow com lazycolumn, puxando o segundo dado da API.
                                    //nextDaysWeather
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
        }
    }
}

@Composable
fun WeatherRow(
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

@Preview(showBackground = true)
@Composable
fun weatherRowPreview(modifier: Modifier = Modifier) {
    WeatherRow(temperatureMin = 13, temperatureMax = 22, weatherCode = 2, time = "Today")
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
    boxes = getBoxSizes(temperatureMin, temperatureMax)
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


suspend fun convertWeatherNextDaysDTOToListDailyWeather(
    weatherNextDaysDTO: WeatherNextDaysDTO
): List<DailyWeather> {
    val converted: List<DailyWeather> =
        weatherNextDaysDTO.daily.weatherCode.indices.map { index ->
            DailyWeather(
                time = getWeekDay(weatherNextDaysDTO.daily.time[index]),
                temperatureMax = weatherNextDaysDTO.daily.temperatureMax[index].toInt(),
                temperatureMin = weatherNextDaysDTO.daily.temperatureMin[index].toInt(),
                weatherCode = weatherNextDaysDTO.daily.weatherCode[index]
            )
        }
    return converted
}


suspend fun convertWeatherTodayDTOToListHourlyWeather(
    weatherTodayDTO: WeatherTodayDTO, days: Int = 0
): List<HourlyWeather> {
    val hourlyMap: List<HourlyWeather>
    if (days == 0) {
        hourlyMap = weatherTodayDTO.hourly.time.indices.mapNotNull { index ->
            if (getHour(weatherTodayDTO.hourly.time[index]) >= getHour(weatherTodayDTO.current.time) && getDay(
                    weatherTodayDTO.hourly.time[index]
                ) == getDay(weatherTodayDTO.current.time) + days
            ) {
                HourlyWeather(
                    time = weatherTodayDTO.hourly.time[index],
                    temperature = weatherTodayDTO.hourly.temperature[index],
                    weatherCode = weatherTodayDTO.hourly.weatherCode[index]
                )
            } else {
                null
            }
        }
    } else {
        hourlyMap = weatherTodayDTO.hourly.time.indices.mapNotNull { index ->
            if (getDay(weatherTodayDTO.hourly.time[index]) == getDay(weatherTodayDTO.current.time) + days) {
                HourlyWeather(
                    time = weatherTodayDTO.hourly.time[index],
                    temperature = weatherTodayDTO.hourly.temperature[index],
                    weatherCode = weatherTodayDTO.hourly.weatherCode[index]
                )
            } else {
                null
            }
        }
    }
    return hourlyMap
}


@Composable
fun Climate(
    cityClimateInfo: ClimateInfo,
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
                    text = cityClimateInfo.city
                )
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = formatToFullDate(cityClimateInfo.date)
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
                    text = cityClimateInfo.temperature.toString() + "°", fontSize = 60.sp
                )
                Text(
                    text = getWeatherDescription(cityClimateInfo.weatherCode), //Colocar weatherCode aqui!
                    fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = getWeatherEmoji(weatherCode = cityClimateInfo.weatherCode),
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
                    text = "\uD83D\uDCA8",
                    fontSize = 32.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = cityClimateInfo.windSpeed.toString() + " m/s",
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
                    text = "\uD83D\uDCA6",
                    fontSize = 32.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = cityClimateInfo.humidity.toString() + "%",
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
                    text = "☔",
                    fontSize = 32.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = String.format("%.0f", cityClimateInfo.rain * 100) + "%",
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
                ClimateHourCard(
                    time = it.time,
                    weatherCode = it.weatherCode,
                    temperature = it.temperature,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ClimateHourCard(
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

data class HourlyWeather(
    val time: String,
    val temperature: Float,
    val weatherCode: Int,
)

data class DailyWeather(
    val time: String,
    val temperatureMin: Int,
    val temperatureMax: Int,
    val weatherCode: Int
)

val stuttgart: ClimateInfo = ClimateInfo(
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

data class ClimateInfo(
    val city: String,
    val weatherCode: Int,
    val temperature: Float,
    val windSpeed: Float,
    val humidity: Int,
    val rain: Float,
    val date: String,
)

fun getWeatherDescription(weatherCode: Int): String {
    return when (weatherCode) {
        0 -> "Clear"
        1, 2, 3 -> "Cloudy"
        45, 48 -> "Fog"
        51, 53, 55 -> "Drizzle"
        56, 57 -> "Icy"
        61, 63, 65 -> "Rain"
        66, 67 -> "Icy"
        71, 73, 75, 77, 85, 86 -> "Snow"
        80, 81, 82 -> "Showers"
        95 -> "Thunder"
        96, 99 -> "Thunderstorm"
        else -> "Unknown"
    }
}

private fun getWeatherEmoji(weatherCode: Int): String {
    return when (weatherCode) {
        0 -> "☀️" // Clear sky
        in 1..3 -> "⛅" // Mainly clear, partly cloudy, and overcast
        45, 48 -> "\uD83E\uDD76" // Fog and depositing rime fog
        in 51..55 -> "🌦️" // Drizzle: Light, moderate, and dense intensity
        in 56..57 -> "🌧️❄️" // Freezing Drizzle: Light and dense intensity
        in 61..63 -> "🌧️" // Rain: Slight and moderate intensity
        65 -> "🌧️🌧️" // Rain: Heavy intensity
        in 66..67 -> "🌧️❄️" // Freezing Rain: Light and heavy intensity
        in 71..75 -> "❄️" // Snow fall: Slight, moderate, and heavy intensity
        77 -> "🌨️" // Snow grains
        in 80..81 -> "🌧️" // Rain showers: Slight and moderate
        82 -> "🌧️🌩️" // Rain showers: Violent
        85 -> "❄️" // Snow showers: Slight
        86 -> "❄️🌨️" // Snow showers: Heavy
        95 -> "⛈️" // Thunderstorm: Slight or moderate
        in 96..99 -> "⛈️⚡" // Thunderstorm with slight and heavy hail
        else -> "❓" // Unknown
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

        Climate(
            stuttgart, weatherTodayDTO = hourlyWeather
        ) { it ->
            println(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClimateHourCardPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        ClimateHourCard(
            modifier = modifier.size(120.dp),
            time = weatherSaoPauloToday.current.time,
            weatherCode = weatherSaoPauloToday.current.weather,
            temperature = 16f
        )
    }
}