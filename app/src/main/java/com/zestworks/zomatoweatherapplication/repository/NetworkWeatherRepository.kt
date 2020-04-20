package com.zestworks.zomatoweatherapplication.repository

import com.zestworks.zomatoweatherapplication.repository.NetworkResult.Error
import com.zestworks.zomatoweatherapplication.repository.NetworkResult.Success

class NetworkWeatherRepository(private val networkService: NetworkService) : WeatherRepository {
    override suspend fun getWeatherInfo(lat: Int, long: Int): NetworkResult<WeatherResponse> {
        return try {
            Success(networkService.getWeatherInfo(lat, long))
        } catch (exception: Exception) {
            Error(exception.message ?: "Network request failed")
        }
    }
}