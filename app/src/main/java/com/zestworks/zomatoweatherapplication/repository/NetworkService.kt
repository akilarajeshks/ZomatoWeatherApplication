package com.zestworks.zomatoweatherapplication.repository

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("forecast?appid=d9bb6a4657d97d2874d597a437414154&units=metric")
    suspend fun getWeatherInfo(
        @Query("lat") lat: Int,
        @Query("lon") long: Int
    ): WeatherResponse
}