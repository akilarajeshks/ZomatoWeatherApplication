package com.zestworks.zomatoweatherapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zestworks.zomatoweatherapplication.common.LCE
import com.zestworks.zomatoweatherapplication.repository.NetworkResult
import com.zestworks.zomatoweatherapplication.repository.Repository
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewModel
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewState
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    private val repository: Repository = mockk()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var weatherViewModel: WeatherViewModel

    private val testObserver: Observer<LCE<WeatherViewState>> = mockk(relaxed = true)

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        weatherViewModel =
            WeatherViewModel(
                repository,
                testCoroutineDispatcher
            )
        weatherViewModel.currentViewState.observeForever(testObserver)
    }

    @Test
    fun `verify if data is loaded on network success`() {
        coEvery { repository.getWeatherInfo() }.returns(NetworkResult.Success(weatherResponse))

        weatherViewModel.onUILoad()
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Content(weatherViewState))
        }
    }

    @Test
    fun `verify if Network Error is handled`() {
        val errorReason = "No Internet"
        coEvery { repository.getWeatherInfo() }.returns(NetworkResult.Error(errorReason))

        weatherViewModel.onUILoad()
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Error(errorReason))
        }
    }

    @Test
    fun `test network call is made only once on rotation`() {
        coEvery { repository.getWeatherInfo() }.returns(NetworkResult.Success(weatherResponse))

        weatherViewModel.onUILoad()
        weatherViewModel.onUILoad()
        testCoroutineDispatcher.advanceUntilIdle()

        verifyOrder {
            testObserver.onChanged(LCE.Loading)
            testObserver.onChanged(LCE.Content(weatherViewState))
        }
    }
}