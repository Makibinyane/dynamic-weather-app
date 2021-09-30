package com.example.dynamic_weather_app.model.forecast

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(@SerializedName("cod") val cod: String,
                                   @SerializedName("message") val message: Int,
                                   @SerializedName("cnt") val cnt: Int,
                                   @SerializedName("list") val list: List<ForecastList>,
                                   @SerializedName("city") val city: City)
