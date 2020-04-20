package com.zestworks.zomatoweatherapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zestworks.zomatoweatherapplication.common.LCE
import com.zestworks.zomatoweatherapplication.repository.NetworkResult
import com.zestworks.zomatoweatherapplication.repository.Repository
import com.zestworks.zomatoweatherapplication.view.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*

class WeatherViewModel(
    private val repository: Repository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _currentViewState =
        MutableLiveData<LCE<WeatherViewState>>()
    val currentViewState = _currentViewState as LiveData<LCE<WeatherViewState>>

    fun fetchWeatherData(location: Location) {
        if (_currentViewState.value == null ) {
            _currentViewState.postValue(LCE.Loading)

            viewModelScope.launch(coroutineDispatcher) {
                when (val weatherNetworkResult =
                    repository.getWeatherInfo(location.lat.toInt(), location.long.toInt())) {
                    is NetworkResult.Success -> {
                        val forecastList = weatherNetworkResult.data.list.groupBy {
                            val cal = Calendar.getInstance()
                            cal.time = it.dtTxt
                            cal.get(Calendar.DAY_OF_WEEK)
                        }.entries.drop(1).take(4).map {
                            ForecastViewState(
                                day = it.key,
                                temp = it.value.first().main.temp.toInt().toString()
                            )
                        }
                        val weatherViewState = WeatherViewState(
                            cityName = weatherNetworkResult.data.city.name,
                            currentDayTemp = weatherNetworkResult.data.list.first().main.temp.toInt()
                                .toString(),
                            forecast = forecastList
                        )
                        _currentViewState.postValue(LCE.Content(viewData = weatherViewState))
                    }
                    is NetworkResult.Error -> {
                        _currentViewState.postValue(LCE.Error(reason = weatherNetworkResult.reason))
                    }
                }
            }
        }
    }

    fun onLocationPermissionDenied() {
        _currentViewState.postValue(LCE.Error("Location Access Denied. Please enable access and retry"))
    }

    fun retryButtonClicked() {
        _currentViewState.postValue(null)
    }
}