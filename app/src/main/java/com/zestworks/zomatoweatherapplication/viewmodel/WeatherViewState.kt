package com.zestworks.zomatoweatherapplication.viewmodel

data class WeatherViewState(
    val cityName: String,
    val currentDayTemp: String,
    val forecast: List<ForecastViewState>
)

data class ForecastViewState(
    val day: Int,
    val temp: String
)