package com.example.dynamic_weather_app.model.current

import com.google.gson.annotations.SerializedName

data class Coordinates(@SerializedName("lon") val lon: Double,
                       @SerializedName("lat") val lat: Double)
