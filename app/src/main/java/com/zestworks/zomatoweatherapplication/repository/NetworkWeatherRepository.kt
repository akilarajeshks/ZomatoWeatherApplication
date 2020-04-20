package com.zestworks.zomatoweatherapplication.repository

class NetworkWeatherRepository(private val networkService: NetworkService) : Repository {
    override suspend fun getWeatherInfo(lat: Int, long: Int): NetworkResult<WeatherResponse> {
        return try {
            NetworkResult.Success(networkService.getWeatherInfo(lat, long))
        } catch (exception: Exception) {
            NetworkResult.Error("Network response failed")
        }
    }
}