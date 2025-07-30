# Weather App ☀️🌧️❄️

Weather is an Android application developed to demonstrate clean architecture, modern UI, and offline-first principles. The app displays current weather and hourly forecast, with support for dark/light themes and local caching via Room. It follows the MVVM architecture and integrates remote and local data sources using best practices.

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

## 📸 Screenshots

*(Add your screenshots here if needed)*

## 🔐 License

The MIT License (MIT)

