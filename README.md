# Weather App ☀️🌧️❄️

Weather is an Android application developed to demonstrate clean architecture, modern UI, and offline-first principles. The app displays current weather and hourly forecast and local caching via Room. It follows the MVVM architecture and integrates remote and local data sources using best practices.

## 📸 Screenshots

<p float="left"> 
  <img src="https://github.com/alanliongar/Weather/blob/master/screenshots/Screenshot_01.png" width="250" /> 
  <img src="https://github.com/alanliongar/Weather/blob/master/screenshots/Screenshot_02.png" width="250" /> 
</p>

## ✨ Features

- 🌤️ **Current & Hourly Forecast**: View current temperature, weather condition, humidity, rain, and wind.
- 📦 **Offline Caching**: Data is cached locally using Room for offline access.
- 🧭 **Location-based Forecast**: Weather data is fetched dynamically based on latitude and longitude.
- 🔄 **Safe API Handling**: Graceful fallback to local cache if remote API fails.
- 🧱 **Modular Architecture**: Clear separation of concerns with ViewModel, Repository, and Data Sources.

## 🛠️ Technologies

- 100% Kotlin
- Jetpack Compose
  - Column, Row, Spacer, Modifier
  - LazyColumn, LazyRow
- Room Database (for offline cache)
- Retrofit (for API integration)
- MVVM Architecture
- Repository Pattern (offline-first)
- Coroutines
- API meteo (open api)
- MapLibre (Library)
- OpenMapStreet (data)

## ⚠️ Requirements

- Android Studio Hedgehog or later
- JDK 21
- Minimum SDK: 21

## 🔐 License
```
The MIT License (MIT)

Copyright (c) 2025 Alan Lucindo Gomes

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

