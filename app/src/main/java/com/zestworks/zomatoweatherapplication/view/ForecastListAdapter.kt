package com.zestworks.zomatoweatherapplication.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zestworks.zomatoweatherapplication.R
import com.zestworks.zomatoweatherapplication.viewmodel.ForecastViewState
import java.util.Calendar.*

class ForecastListAdapter(private val forecastList: List<ForecastViewState>) :
    RecyclerView.Adapter<ForecastListAdapter.ForecastItemHolder>() {

    class ForecastItemHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.forecast_day)
        val temperatureTextView: TextView = view.findViewById(R.id.forecast_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemHolder {
        return ForecastItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ForecastItemHolder, position: Int) {
        holder.dayTextView.text =  when(forecastList[position].day){
            SUNDAY -> "Sunday"
            MONDAY -> "Monday"
            TUESDAY -> "Tuesday"
            WEDNESDAY -> "Wednesday"
            THURSDAY -> "Thursday"
            FRIDAY -> "Friday"
            SATURDAY -> "Saturday"
            else -> "-- check response --"
        }
        val temp=forecastList[position].temp
        holder.temperatureTextView.text = "$temp℃"
    }
}