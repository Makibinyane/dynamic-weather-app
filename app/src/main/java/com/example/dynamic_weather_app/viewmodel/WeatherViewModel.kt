package com.example.dynamic_weather_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamic_weather_app.model.current.WeatherResponse
import com.example.dynamic_weather_app.model.forecast.ForecastList
import com.example.dynamic_weather_app.repository.WeatherRepository
import com.example.dynamic_weather_app.util.WeatherUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt


@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherResponse?>()
    val weatherForecastLiveData = MutableLiveData<List<ForecastList?>>()
    private val forecast = mutableListOf<ForecastList>()
    val errorMessage = MutableLiveData<String>()
    val showProgress = MutableLiveData<Boolean>()

    var currentWeather: WeatherResponse? = null

    fun fetchCurrentWeather(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            showProgress.value = true
            val response = repository.getCurrentWeatherData(longitude, latitude)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    weatherLiveData.postValue(response.body())
                    currentWeather = response.body()
                    WeatherUtil.currentMainWeather = response.body()?.weather?.get(0)?.main.toString()
                    showProgress.value = false
                } else {
                    onError(response.message())
                }
            }
        }
    }

    fun fetchWeatherForecast(longitude: Double, latitude: Double) {
        viewModelScope.launch {
            showProgress.value = true
            val response = repository.getWeatherForecastData(longitude, latitude)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.list?.forEach {
                        if (it.dt_txt.contains("00:00:00")) {
                            forecast.add(it)
                        }
                    }
                    weatherForecastLiveData.postValue(forecast)
                    showProgress.value = false
                } else {
                    onError(response.message())
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        showProgress.value = false
    }
}