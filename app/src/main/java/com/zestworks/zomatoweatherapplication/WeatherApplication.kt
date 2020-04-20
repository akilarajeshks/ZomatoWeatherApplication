package com.zestworks.zomatoweatherapplication

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zestworks.zomatoweatherapplication.repository.NetworkService
import com.zestworks.zomatoweatherapplication.repository.NetworkWeatherRepository
import com.zestworks.zomatoweatherapplication.repository.WeatherRepository
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val module = module {
            single { provideGson() }
            single {  provideRetrofit(get()) }
            single<WeatherRepository> { NetworkWeatherRepository(get()) }
            viewModel { WeatherViewModel(get(), Dispatchers.IO) }
        }

        startKoin {
                androidContext(this@WeatherApplication)
                modules(module)
        }
    }

    private fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-mm-dd HH:mm:ss").create()
    }

    private fun provideRetrofit(gson: Gson): NetworkService {
        val baseURL = "https://api.openweathermap.org/data/2.5/"
        val retrofit = Retrofit.Builder().baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
        return retrofit.create(NetworkService::class.java)
    }
}