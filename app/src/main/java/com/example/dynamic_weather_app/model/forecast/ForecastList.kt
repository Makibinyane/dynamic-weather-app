package com.example.dynamic_weather_app.model.forecast

import com.example.dynamic_weather_app.model.current.Clouds
import com.example.dynamic_weather_app.model.current.Weather
import com.example.dynamic_weather_app.model.current.Wind
import com.google.gson.annotations.SerializedName

data class ForecastList(@SerializedName("dt") val dt: Int,
                        @SerializedName("main") val main: Main,
                        @SerializedName("weather") val weather: List<Weather>,
                        @SerializedName("clouds") val clouds: Clouds,
                        @SerializedName("wind") val wind: Wind,
                        @SerializedName("visibility") val visibility: Int,
                        @SerializedName("pop") val pop: Any,
                        @SerializedName("sys") val sys: Sys,
                        @SerializedName("dt_txt") val dt_txt: String)
