package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather.ui.theme.WeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                val apiService = WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Climate(cityClimateInfo: ClimateInfo, modifier: Modifier = Modifier) {
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
                    fontSize = 14.sp,
                    text = cityClimateInfo.city
                )
                Text(
                    modifier = modifier.align(Alignment.Start),
                    fontSize = 14.sp,
                    text = cityClimateInfo.date
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
                    fontSize = 14.sp
                )
            }
            Text(
                text = getWeatherEmoji(99),
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
                    text = String.format("%.0f", cityClimateInfo.humidity * 100).toString() + "%"
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
                    text = String.format("%.0f", cityClimateInfo.rain * 100).toString() + "%"
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
        Spacer(modifier = modifier.size(10.dp))
        Row {
            Column() {
                Card(modifier = modifier.size(120.dp)) {
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f)
                            .padding(top = 8.dp),
                        text = "10 am",
                        fontSize = 14.sp
                    )
                    Text(
                        text = getWeatherEmoji(0),
                        fontSize = 28.sp, // ou ajuste conforme quiser
                        modifier = modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f)
                            .padding(top = 8.dp),
                        text = "16°",
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = modifier.size(10.dp))
            Column() {
                Card(modifier = modifier.size(120.dp)) {
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f)
                            .padding(top = 8.dp),
                        text = "11 am",
                        fontSize = 14.sp
                    )
                    Text(
                        text = getWeatherEmoji(65),
                        fontSize = 28.sp, // ou ajuste conforme quiser
                        modifier = modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f)
                            .padding(top = 8.dp),
                        text = "18°",
                        fontSize = 14.sp
                    )
                }
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherTheme {
        Climate(stuttgart)
    }
}

val stuttgart: ClimateInfo =
    ClimateInfo(
        "Stuttgart",
        99,
        18,
        10,
        .98f,
        1.0f,
        "12 September, Sunday"
    )

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

data class ClimateInfo(
    val city: String,
    val weatherCode: Int,
    val temperature: Int,
    val windSpeed: Int,
    val humidity: Float,
    val rain: Float,
    val date: String,
)