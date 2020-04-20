package com.zestworks.zomatoweatherapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zestworks.zomatoweatherapplication.common.LCE
import com.zestworks.zomatoweatherapplication.repository.NetworkResult
import com.zestworks.zomatoweatherapplication.repository.WeatherRepository
import com.zestworks.zomatoweatherapplication.viewmodel.Location
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewModel
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    private val weatherRepository: WeatherRepository = mockk()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var weatherViewModel: WeatherViewModel

    private val testObserver: Observer<LCE<WeatherViewState>> = mockk(relaxed = true)

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val location =
        Location(2.0, 2.0)

    @Before
    fun setup() {
        weatherViewModel =
            WeatherViewModel(
                weatherRepository,
                testCoroutineDispatcher
            )
        weatherViewModel.currentViewState.observeForever(testObserver)
    }

    @Test
    fun `verify if data is loaded on network success`() {
        coEvery { weatherRepository.getWeatherInfo(location.lat.toInt(), location.long.toInt()) }.returns(NetworkResult.Success(weatherResponse))

        weatherViewModel.fetchWeatherData(location=location)
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Content(weatherViewState))
        }
    }

    @Test
    fun `verify if Network Error is handled`() {
        val errorReason = "No Internet"
        coEvery { weatherRepository.getWeatherInfo(location.lat.toInt(), location.long.toInt()) }.returns(NetworkResult.Error(errorReason))

        weatherViewModel.fetchWeatherData(location=location)
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Error(errorReason))
        }
    }

    @Test
    fun `test network call is made only once on rotation`() {
        coEvery { weatherRepository.getWeatherInfo(location.lat.toInt(), location.long.toInt()) }.returns(NetworkResult.Success(weatherResponse))

        weatherViewModel.fetchWeatherData(location=location)
        weatherViewModel.fetchWeatherData(location=location)
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Content(weatherViewState))
        }
    }

    @Test
    fun `test retry button calls the network`(){
        val errorReason = "No Internet"
        coEvery { weatherRepository.getWeatherInfo(location.lat.toInt(), location.long.toInt()) }.returns(NetworkResult.Error(errorReason))

        weatherViewModel.fetchWeatherData(location=location)
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Error(errorReason))
        }

        coEvery { weatherRepository.getWeatherInfo(location.lat.toInt(), location.long.toInt()) }.returns(NetworkResult.Success(weatherResponse))

        weatherViewModel.retryButtonClicked()
        weatherViewModel.fetchWeatherData(location=location)

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Content(weatherViewState))
        }
    }

    @Test
    fun `test for location permission denied`(){
        weatherViewModel.onLocationPermissionDenied()

        val errorReason = "Location Access Denied. Please enable access and retry"

        verify{
            testObserver.onChanged(LCE.Error(errorReason))
        }
    }
}