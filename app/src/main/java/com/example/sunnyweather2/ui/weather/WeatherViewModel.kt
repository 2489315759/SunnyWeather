package com.example.sunnyweather2.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather2.logic.Repository
import com.example.sunnyweather2.logic.model.Location
import com.example.sunnyweather2.logic.model.Weather

class WeatherViewModel:ViewModel() {
    private val locationLiveData=MutableLiveData<Location>()
    var locationLng=""
    var locationLat=""
    var placeName=""
    //当检测到locationLiveData发生改变后将会调用仓库中的refreshWeather
    val weatherLiveData=Transformations.switchMap(locationLiveData){
        location->Repository.refreshWeather(location.lng,location.lat,placeName)
    }
    fun refreshWeather(lng:String,lat:String){
        //将获得的经纬度坐标封装之后赋值为locationLiveData
        locationLiveData.value= Location(lng, lat)
    }
}