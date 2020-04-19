package com.zestworks.zomatoweatherapplication.repository

interface Repository {
    suspend fun getWeatherInfo(): NetworkResult<WeatherResponse>
}