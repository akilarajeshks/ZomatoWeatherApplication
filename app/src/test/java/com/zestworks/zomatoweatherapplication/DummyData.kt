package com.zestworks.zomatoweatherapplication

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import com.zestworks.zomatoweatherapplication.repository.WeatherResponse
import com.zestworks.zomatoweatherapplication.viewmodel.ForecastViewState
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewState
import java.io.File

val jsonResponse =
    File("src/test/java/com/zestworks/zomatoweatherapplication/successfulResponse.json").readText()
val gson: Gson = GsonBuilder()
    .setDateFormat("yyyy-mm-dd HH:mm:ss").create()
val weatherResponse: WeatherResponse = gson.fromJson(jsonResponse, WeatherResponse::class.java)

val weatherViewState =
    WeatherViewState(
        cityName = "Shuzenji", currentDayTemp = "10", forecast = listOf(
            ForecastViewState(
                day = 2,
                temp = "9"
            ),
            ForecastViewState(
                day = 3,
                temp = "16"
            ),
            ForecastViewState(
                day = 4,
                temp = "12"
            ),
            ForecastViewState(
                day = 5,
                temp = "10"
            )
        )
    )