package com.example.dynamic_weather_app.service

import com.example.dynamic_weather_app.model.current.WeatherResponse
import com.example.dynamic_weather_app.model.forecast.ForecastList
import com.example.dynamic_weather_app.model.forecast.WeatherForecastResponse
import com.example.dynamic_weather_app.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface WeatherService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "x-api-key: $API_KEY"
    )
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(@Query("lon") longitude: Double,
                                @Query("lat") latitude: Double): Response<WeatherResponse>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "x-api-key: $API_KEY"
    )
    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(@Query("lon") longitude: Double,
                                   @Query("lat") latitude: Double,
                                   @Query("appid") appid: String): Response<WeatherForecastResponse>
}