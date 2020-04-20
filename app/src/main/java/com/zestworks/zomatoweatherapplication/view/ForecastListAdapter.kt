package com.zestworks.zomatoweatherapplication.view

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

    class ForecastItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayTextView: TextView = view.findViewById(R.id.forecast_day)
        val temperatureTextView: TextView = view.findViewById(R.id.forecast_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastItemHolder =
        ForecastItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        )

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ForecastItemHolder, position: Int) {
        holder.apply {
            dayTextView.text = when (forecastList[position].day) {
                SUNDAY -> itemView.context.getString(R.string.sunday)
                MONDAY -> itemView.context.getString(R.string.monday)
                TUESDAY -> itemView.context.getString(R.string.tuesday)
                WEDNESDAY -> itemView.context.getString(R.string.wednesday)
                THURSDAY -> itemView.context.getString(R.string.thursday)
                FRIDAY -> itemView.context.getString(R.string.friday)
                SATURDAY -> itemView.context.getString(R.string.saturday)
                else -> "-- check response --"
            }
            temperatureTextView.text =
                holder.itemView.context.getString(R.string.celsius, forecastList[position].temp)
        }
    }
}