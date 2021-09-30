package com.example.dynamic_weather_app.util

import com.example.dynamic_weather_app.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object WeatherUtil {

    lateinit var currentMainWeather: String

    fun convertKelvinToCelsius(kelvinTemperature: Double?): Int? {
        return kelvinTemperature?.minus(273)?.roundToInt()
    }

    fun getDay(epoch: Int): String {
        val cal: Calendar = Calendar.getInstance()
        val current = epoch.toLong()
        cal.timeInMillis = current * 1000
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.time)
    }

    fun getWeatherSymbol(mainWeather: String) : Int {
        var weatherSymbol = 0
        when {
            mainWeather.contains("Clear") -> {
                weatherSymbol = R.drawable.ic_clear
            }
            mainWeather.contains("Rain") -> {
                weatherSymbol = R.drawable.ic_rain
            }
            mainWeather.contains("Clouds") -> {
                weatherSymbol = R.drawable.ic_partly_sunny
            }
        }
        return weatherSymbol
    }
}