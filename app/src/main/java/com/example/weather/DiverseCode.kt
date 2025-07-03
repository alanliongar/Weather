package com.example.weather

import kotlinx.coroutines.runBlocking

fun main() {
    val weatherNextDaysService =
        WeatherRetrofitClient.retrofitInstance.create(WeatherNextDaysService::class.java)
    val weatherTodayService =
        WeatherRetrofitClient.retrofitInstance.create(WeatherTodayService::class.java)
    runBlocking {
        val rightCoordinatesNextDaysWeather: RightCoordinatesDTO =
            weatherNextDaysService.getCoordinates(-23.75f, -46.68f)

        val weatherNextDaysMock: WeatherNextDaysDTO =
            weatherNextDaysService.getNextDaysWeather(rightCoordinatesNextDaysWeather.latitude, rightCoordinatesNextDaysWeather.longitude)

        val rightCoordinatesWeatherToday: RightCoordinatesDTO =
            weatherTodayService.getCoordinates(-23.78f, -46.69f)
        /*val weatherTodayMock: WeatherTodayDTO = weatherTodayService.getTodayWeather(
            latitude = rightCoordinatesWeatherToday.latitude,
            longitude = rightCoordinatesWeatherToday.longitude
        )*/
        println(weatherNextDaysMock.toString())
        //println(weatherTodayMock.toString())
    }
}


//Estudar callback e response (do retrofit), saber implementar ambos
//completar o APP.