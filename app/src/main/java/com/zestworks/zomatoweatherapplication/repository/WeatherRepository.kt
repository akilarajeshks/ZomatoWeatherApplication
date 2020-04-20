package com.zestworks.zomatoweatherapplication.repository

interface WeatherRepository {
    suspend fun getWeatherInfo(lat: Int, long: Int): NetworkResult<WeatherResponse>
}