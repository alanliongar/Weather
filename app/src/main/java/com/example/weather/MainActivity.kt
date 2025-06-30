package com.example.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "detail icon",
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
                    text = cityClimateInfo.weatherStatus,
                    fontSize = 14.sp
                )
            }
            Icon(
                modifier = modifier
                    .weight(1f)
                    .size(90.dp),
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Weather icon"
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(modifier = modifier.weight(1f)) {
                Icon(
                    modifier = modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "wind icon"
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
                Icon(
                    modifier = modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "humidity icon"
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
                Icon(
                    modifier = modifier
                        .size(28.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "rain icon"
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
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterHorizontally)
                            .weight(1f),
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "weather icon",
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f),
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
                        text = "10 am",
                        fontSize = 14.sp
                    )
                    Icon(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterHorizontally)
                            .weight(1f),
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "weather icon",
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(1f),
                        text = "16°",
                        fontSize = 14.sp
                    )
                }
            }
        }

        Image(
            modifier = modifier.fillMaxWidth().height(200.dp).padding(16.dp),
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
        "Thunderstorm",
        18,
        10,
        .98f,
        1.0f,
        "12 September, Sunday"
    )

data class ClimateInfo(
    val city: String,
    val weatherStatus: String,
    val temperature: Int,
    val windSpeed: Int,
    val humidity: Float,
    val rain: Float,
    val date: String,
)