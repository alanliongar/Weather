package com.example.weather

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.ui.theme.WeatherTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import retrofit2.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            WeatherTheme {
                var todayWeather by remember { mutableStateOf<WeatherTodayDTO?>(null) }
                val weatherTodayService =
                    WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)
                val callTodayWeather = weatherTodayService.getTodayWeather(-23.78f, -46.69f)
                callTodayWeather.enqueue(object : Callback<WeatherTodayDTO> {
                    override fun onResponse(
                        call: Call<WeatherTodayDTO>,
                        response: Response<WeatherTodayDTO>
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

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Spacer(modifier = Modifier.size(20.dp))
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
                        val hourlyMap: List<HourlyWeather>
                        runBlocking {
                            hourlyMap =
                                convertWeatherTodayDTOToListHourlyWeather(weatherTodayDTO = todayWeather!!)
                            delay(1000)
                        }
                        Climate(climateInfo, weatherTodayDTO = hourlyMap, modifier = Modifier)
                    } else {
                        Text("Error")
                    }

                }
            }
        }
    }
}

suspend fun convertWeatherTodayDTOToListHourlyWeather(weatherTodayDTO: WeatherTodayDTO): List<HourlyWeather> {
    val hourlyMap: List<HourlyWeather> =
        weatherTodayDTO.hourly.time.indices.map { index ->
            HourlyWeather(
                time = weatherTodayDTO.hourly.time[index],
                temperature = weatherTodayDTO.hourly.temperature[index],
                weatherCode = weatherTodayDTO.hourly.weatherCode[index]
            )
        }
    return hourlyMap
}


@Composable
fun Climate(
    cityClimateInfo: ClimateInfo,
    modifier: Modifier = Modifier,
    weatherTodayDTO: List<HourlyWeather>
) {


    Column(modifier = modifier.padding(10.dp)) {
        Row(modifier = modifier.fillMaxWidth()) {
            Column(
                modifier = modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    modifier = modifier
                        .align(Alignment.Start),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = cityClimateInfo.city
                )
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = formatToFullDate(cityClimateInfo.date)
                )
            }
            Icon(
                modifier = modifier
                    .align(Alignment.CenterVertically)
                    .size(28.dp),
                imageVector = Icons.Filled.GridView,
                contentDescription = "Grid view"
            )
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
                    text = cityClimateInfo.temperature.toString() + "°",
                    fontSize = 56.sp
                )
                Text(
                    text = getWeatherDescription(cityClimateInfo.weatherCode), //Colocar weatherCode aqui!
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
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
                    fontSize = 28.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = cityClimateInfo.windSpeed.toString() + " m/s"
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Wind"
                )
            }
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = "\uD83D\uDCA6",
                    fontSize = 28.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = cityClimateInfo.humidity.toString() + "%"
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Humidity"
                )
            }
            Column(modifier = modifier.weight(1f)) {
                Text(
                    text = "☔",
                    fontSize = 28.sp,
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = String.format("%.0f", cityClimateInfo.rain * 100) + "%"
                )
                Text(
                    modifier = modifier.align(Alignment.CenterHorizontally),
                    text = "Rain"
                )
            }
        }
        Row(modifier = modifier) {
            Text(
                text = "Today",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.size(14.dp))
            Text(
                text = "Tomorrow",
                fontSize = 14.sp
            )
            Spacer(modifier = modifier.size(14.dp))
            Text(
                text = "Next 10 days",
                fontSize = 14.sp
            )
        }

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

        Image(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "map"
        )
    }
}

@Composable
fun ClimateHourCard(
    time: String,
    weatherCode: Int,
    temperature: Float,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.size(120.dp)) {
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .padding(top = 8.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            text = formatToHourPeriod(time),
        )
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f),
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            text = getWeatherEmoji(weatherCode),
        )
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .padding(top = 8.dp),
            text = String.format(Locale.US, "%.1f", temperature) + "°",
            fontSize = 14.sp,
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

val stuttgart: ClimateInfo =
    ClimateInfo(
        "Stuttgart",
        2,
        18f,
        10f,
        98,
        1.0f,
        "2021-09-12T10:00"
    )

val weatherStuttgart = WeatherTodayDTO(
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

data class ClimateInfo(
    val city: String,
    val weatherCode: Int,
    val temperature: Float,
    val windSpeed: Float,
    val humidity: Int,
    val rain: Float,
    val date: String,
)

@Preview(showBackground = true)
@Composable
fun ClimatePreview() {
    WeatherTheme {
        var hourlyWeather: List<HourlyWeather>
        runBlocking {
            hourlyWeather = convertWeatherTodayDTOToListHourlyWeather(
                weatherStuttgart
            )
            delay(500)
        }

        Climate(
            stuttgart, weatherTodayDTO = hourlyWeather
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClimateHourCardPreview(modifier: Modifier = Modifier) {
    WeatherTheme {
        ClimateHourCard(
            modifier = modifier.size(120.dp),
            time = weatherStuttgart.current.time,
            weatherCode = weatherStuttgart.current.weather,
            temperature = 16f
        )
    }
}

private fun getWeatherDescription(weatherCode: Int): String {
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
        0 -> "☀" // Clear sky
        1, 2, 3 -> "\uD83C\uDF25" // Cloudy: Mainly clear, partly cloudy, overcast
        45, 48 -> "\uD83D\uDCA6" // Fog
        51, 53, 55 -> "\uD83C\uDF27\uFE0F" // Drizzle
        56, 57 -> "\u2744\uD83D\uDCA6" // Freezing Drizzle
        61, 63 -> "☔" // Rain: slight and moderate
        65 -> "\uD83C\uDF27\uFE0F" // Rain: heavy
        66, 67 -> "\u2744☔" // Freezing Rain
        71, 73 -> "\uD83C\uDF28" // Snow: slight, moderate
        75 -> "\u2744" // Snow: heavy
        77 -> "\u2744" // Snow grains
        80, 81 -> "☔" // Rain showers: slight, moderate
        82 -> "\uD83C\uDF27\uFE0F" // Rain showers: violent
        85 -> "\u2744" // Snow showers slight
        86 -> "\uD83C\uDF28" // Snow showers heavy
        95 -> "\uD83C\uDF29" // Thunderstorm: slight/moderate
        96, 99 -> "\uD83C\uDF29⚡" // Thunderstorm + hail
        else -> "" // Unknown
    }
}

fun formatToHourPeriod(isoDateTime: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val dateTime = LocalDateTime.parse(isoDateTime, formatter)
    return dateTime.format(DateTimeFormatter.ofPattern("hh a", Locale.ENGLISH)).lowercase()
}

fun formatToFullDate(isoDateTime: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val dateTime = LocalDateTime.parse(isoDateTime, formatter)
    return dateTime.format(DateTimeFormatter.ofPattern("dd MMMM, EEEE", Locale.ENGLISH))
}