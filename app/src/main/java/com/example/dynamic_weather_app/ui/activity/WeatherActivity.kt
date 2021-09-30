package com.example.dynamic_weather_app.ui.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.dynamic_weather_app.R
import com.example.dynamic_weather_app.databinding.ActivityWeatherBinding
import com.example.dynamic_weather_app.ui.adapter.TemperatureDaysAdapter
import com.example.dynamic_weather_app.util.WeatherUtil
import com.example.dynamic_weather_app.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityWeatherBinding
    private var locationManager: LocationManager? = null
    private var provider: String? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                setLocation(location)
            }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE
            )
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_COARSE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_forest -> {
                setLayoutBackgroundImage(resources.getString(R.string.forest_weather_type))
                true
            }
            R.id.action_sea -> {
                setLayoutBackgroundImage(resources.getString(R.string.sea_weather_type))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setLayoutBackgroundImage(type: String) {
        viewModel.currentWeather?.let {
            val weatherDetails = it.weather[0].main
            when {
                weatherDetails.contains(resources.getString(R.string.clouds)) -> {
                    if (type == resources.getString(R.string.sea_weather_type)) {
                        binding.currentTempLayout.setBackgroundResource(R.drawable.sea_cloudy)
                    } else {
                        binding.currentTempLayout.setBackgroundResource(R.drawable.forest_cloudy)
                    }
                }
                weatherDetails.contains(resources.getString(R.string.rain)) -> {
                    if (type == resources.getString(R.string.sea_weather_type)) {
                        binding.currentTempLayout.setBackgroundResource(R.drawable.sea_rainy)
                    } else {
                        binding.currentTempLayout.setBackgroundResource(R.drawable.forest_rainy)
                    }
                }
                else -> {
                    if (type == resources.getString(R.string.sea_weather_type)) {
                        binding.currentTempLayout.setBackgroundResource(R.drawable.sea_sunnypng)
                    } else {
                        binding.currentTempLayout.setBackgroundResource(R.drawable.forest_sunny)
                    }
                }
            }
        }
    }

    private fun setLocation(location: Location) {
        fetchCurrentWeatherDetails(location)
        fetchWeatherForecastDetails(location)

        viewModel.showProgress.observe(this) { showProgress ->
            if (showProgress) {
                binding.progressbar.visibility = View.VISIBLE
            } else {
                binding.progressbar.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Snackbar.make(
                    binding.countryNamesRecyclerView,
                    errorMessage,
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    binding.countryNamesRecyclerView,
                    resources.getString(R.string.something_went_wrong_message),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun fetchCurrentWeatherDetails(location: Location) {
        viewModel.fetchCurrentWeather(location.longitude, location.latitude)

        viewModel.weatherLiveData.observe(this) { weatherData ->

            val currentTemperature = WeatherUtil.convertKelvinToCelsius(weatherData?.main?.temp)
            binding.txtCurrentTemperature.text = resources.getString(
                R.string.temperature_text,
                currentTemperature.toString(),
                0x00B0.toChar()
            )

            when {
                weatherData?.weather?.get(0)?.main?.contains(resources.getString(R.string.clear)) == true -> {
                    binding.txtCurrentTemperatureDesc.text =
                        resources.getString(R.string.sunny)
                    binding.currentTempLayout.setBackgroundResource(R.drawable.sea_sunnypng)
                    binding.mainLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.sunny
                        )
                    )
                }
                weatherData?.weather?.get(0)?.main?.contains(resources.getString(R.string.rain)) == true -> {
                    binding.txtCurrentTemperatureDesc.text =
                        resources.getString(R.string.rainy)

                    binding.currentTempLayout.setBackgroundResource(R.drawable.sea_rainy)
                    binding.mainLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.rainy
                        )
                    )
                }
                else -> {
                    binding.txtCurrentTemperatureDesc.text =
                        resources.getString(R.string.cloudy)
                    binding.currentTempLayout.setBackgroundResource(R.drawable.sea_cloudy)
                    binding.mainLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.cloudy
                        )
                    )
                }
            }

            val minTemperature = WeatherUtil.convertKelvinToCelsius(weatherData?.main?.temp_min)
            val maxTemperature = WeatherUtil.convertKelvinToCelsius(weatherData?.main?.temp_max)

            binding.txtMin.text = resources.getString(
                R.string.temperature_text,
                minTemperature.toString(),
                0x00B0.toChar()
            )
            binding.txtMax.text = resources.getString(
                R.string.temperature_text,
                maxTemperature.toString(),
                0x00B0.toChar()
            )
            binding.txtCurrent.text = resources.getString(
                R.string.temperature_text,
                currentTemperature.toString(),
                0x00B0.toChar()
            )
        }
    }

    private fun fetchWeatherForecastDetails(location: Location) {
        viewModel.fetchWeatherForecast(location.longitude, location.latitude)
        viewModel.weatherForecastLiveData.observe(this) { weatherForecast ->

            val temperaturesAdapter = TemperatureDaysAdapter()

            binding.countryNamesRecyclerView.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = temperaturesAdapter
                hasFixedSize()
            }
            temperaturesAdapter.submitList(weatherForecast)
        }
    }

    companion object {
        private const val REQUEST_FINE = 100
        private const val REQUEST_COARSE = 101

    }






    /*
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.weatherFragment)
        )

        //Setting up the action bar
        setupActionBarWithNavController(navController, appBarConfiguration)
    } */
}