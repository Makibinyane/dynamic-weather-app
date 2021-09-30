package com.example.dynamic_weather_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamic_weather_app.R
import com.example.dynamic_weather_app.model.forecast.ForecastList
import com.example.dynamic_weather_app.util.WeatherUtil

class TemperatureDaysAdapter :
    ListAdapter<ForecastList, TemperatureDaysAdapter.TemperatureDayViewHolder>(
        TemperatureDaysDiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemperatureDaysAdapter.TemperatureDayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.days_temperature_list_item,
            parent,
            false
        )
        return TemperatureDayViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TemperatureDaysAdapter.TemperatureDayViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.setIsRecyclable(false)
        holder.bind(item)
    }

    inner class TemperatureDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtTemperature: TextView = itemView.findViewById(R.id.txtTemperature)
        private var imgSymbol: ImageView = itemView.findViewById(R.id.imgSymbol)
        private var txtDay: TextView = itemView.findViewById(R.id.txtDay)

        fun bind(forecast: ForecastList) {

            val currentTemperature = WeatherUtil.convertKelvinToCelsius(forecast.main.temp)
            txtTemperature.text = itemView.context.resources.getString(
                R.string.temperature_text,
                currentTemperature.toString(),
                0x00B0.toChar()
            )
            txtDay.text = WeatherUtil.getDay(forecast.dt)

            val weatherSymbol = WeatherUtil.getWeatherSymbol(forecast.weather[0].main)
            imgSymbol.setColorFilter(ContextCompat.getColor(itemView.context, R.color.white));
            imgSymbol.setImageResource(weatherSymbol)

            WeatherUtil.currentMainWeather.let { mainWeather ->

                when {
                    mainWeather.contains(itemView.context.resources.getString(R.string.clear)) -> {
                        itemView.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.sunny
                            )
                        )
                    }
                    mainWeather.contains(itemView.context.resources.getString(R.string.rain)) -> {
                        itemView.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.rainy
                            )
                        )
                    }
                    else -> {
                        itemView.setBackgroundColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.cloudy
                            )
                        )
                    }
                }
            }
        }
    }

    class TemperatureDaysDiffCallback : DiffUtil.ItemCallback<ForecastList>() {
        override fun areItemsTheSame(
            oldItem: ForecastList,
            newItem: ForecastList
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastList,
            newItem: ForecastList
        ): Boolean {
            return oldItem.dt == newItem.dt
                    && oldItem.wind == oldItem.wind
        }
    }
}