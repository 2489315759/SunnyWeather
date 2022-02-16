package com.example.sunnyweather2.logic.model

import com.google.gson.annotations.SerializedName
//查询当日的数据模型
data class RealtimeResponse(val status:String,val result:Result) {
    data class Result(val realtime:Realtime)
    data class Realtime(val skycon:String,val temperature:Float,@SerializedName("air_quality")val airQuality:AirQuality)
    data class AirQuality(val aqi:AQI)
    data class AQI(val chn:Float)
}