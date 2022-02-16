package com.example.sunnyweather2.logic.network


import com.example.sunnyweather2.SunnyWeatherApplication
import com.example.sunnyweather2.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//向服务器发送请求，获取数据。
interface PlaceService {
    //将seatchPlaces的返回值声明为Call<PlaceResponse>，Retrofit会自动将服务器返回的JSON数据解析为PlaceResponse对象。
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query")query:String): Call<PlaceResponse>
}