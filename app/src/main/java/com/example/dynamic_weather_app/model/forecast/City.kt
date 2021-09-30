package com.example.dynamic_weather_app.model.forecast

import com.example.dynamic_weather_app.model.current.Coordinates
import com.google.gson.annotations.SerializedName

data class City( @SerializedName("id") val id: Int,
                 @SerializedName("name") val name: String,
                 @SerializedName("coord") val coord: Coordinates,
                 @SerializedName("country") val country: String,
                 @SerializedName("population") val population: Int,
                 @SerializedName("timezone") val timezone: Int,
                 @SerializedName("sunrise") val sunrise: Int,
                 @SerializedName("sunset") val sunset: Int)
