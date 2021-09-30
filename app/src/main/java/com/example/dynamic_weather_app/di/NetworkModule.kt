package com.example.dynamic_weather_app.di

import com.example.dynamic_weather_app.service.WeatherService
import com.example.dynamic_weather_app.service.WeatherServiceClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideWeatherService(): WeatherService {
        return WeatherServiceClient.create()
    }
}