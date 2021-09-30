package com.example.dynamic_weather_app.repository

import com.example.dynamic_weather_app.model.current.WeatherResponse
import com.example.dynamic_weather_app.model.forecast.WeatherForecastResponse
import com.example.dynamic_weather_app.service.WeatherService
import com.example.dynamic_weather_app.util.API_KEY
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val service: WeatherService) {
    suspend fun getCurrentWeatherData(longitude: Double, latitude: Double): Response<WeatherResponse> {
        return service.getCurrentWeather(longitude, latitude)
    }

    suspend fun getWeatherForecastData(longitude: Double,
                                   latitude: Double): Response<WeatherForecastResponse> {
        return service.getWeatherForecast(longitude, latitude, API_KEY)
    }
}