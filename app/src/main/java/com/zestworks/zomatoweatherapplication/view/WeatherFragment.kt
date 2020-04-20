package com.zestworks.zomatoweatherapplication.view

import android.Manifest.permission
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.zestworks.zomatoweatherapplication.R
import com.zestworks.zomatoweatherapplication.common.LCE
import com.zestworks.zomatoweatherapplication.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.android.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val weatherViewModel: WeatherViewModel by viewModel()
    override fun onStart() {
        super.onStart()
        weatherViewModel.currentViewState.observe(activity!!, Observer {
            when (it) {
                LCE.None->{
                    getLocationData()
                }
                is LCE.Content ->{
                    content_group.visibility=View.VISIBLE
                    error_group.visibility=View.GONE
                    loading_group.visibility=View.GONE

                    parent.setBackgroundColor(Color.WHITE)
                    city_text_view.text = it.viewData.cityName
                    temperature.text = it.viewData.currentDayTemp+"℃"
                    if (forecast_recycler.adapter == null){
                        forecast_recycler.apply {
                            adapter = ForecastListAdapter(it.viewData.forecast)
                            layoutManager = LinearLayoutManager(this@WeatherFragment.context)
                        }
                    }else{
                        forecast_recycler.adapter?.notifyDataSetChanged()
                    }
                }
                is LCE.Error -> {
                    content_group.visibility=View.GONE
                    error_group.visibility=View.VISIBLE
                    loading_group.visibility=View.GONE
                    parent.setBackgroundColor(Color.parseColor("#E85959"))
                    btn_retry.setOnClickListener { getLocationData() }
                }
                LCE.Loading ->{
                    parent.setBackgroundColor(Color.WHITE)
                    content_group.visibility=View.GONE
                    error_group.visibility=View.GONE
                    loading_group.visibility=View.VISIBLE

                    ObjectAnimator.ofFloat(loader, "rotation", 180f, 0f).apply {
                        duration = 2000
                        repeatCount = ObjectAnimator.INFINITE
                        repeatMode = ObjectAnimator.RESTART
                        start()
                    }
                }
            }
        })
        //weatherViewModel.onUILoad()
    }

    private fun getLocationData() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(permission.ACCESS_COARSE_LOCATION),
                PERMISSION_CONSTANT
            )
        } else {
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it == null){
                    weatherViewModel.onLocationPermissionDenied()
                }else{
                    weatherViewModel.onLocationFetched(it.latitude,it.longitude)
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CONSTANT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                        if (it == null){
                            weatherViewModel.onLocationPermissionDenied()
                        }else{
                            weatherViewModel.onLocationFetched(it.latitude,it.longitude)
                        }
                    }
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    weatherViewModel.onLocationPermissionDenied()
                }
                return
            }
        }
    }

    companion object {
        private const val PERMISSION_CONSTANT = 1
    }

}