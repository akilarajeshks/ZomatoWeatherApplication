package com.zestworks.zomatoweatherapplication.repository

interface Repository {
    suspend fun getWeatherInfo(lat: Int, long: Int): NetworkResult<WeatherResponse>
}