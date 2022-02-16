package com.example.sunnyweather2.logic.network

import android.util.Log
import kotlinx.coroutines.supervisorScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
//网络数据源访问入口。
object SunnyWeatherNetwork {
    //创建了一个PlaceService的动态代理接口
    private val placeService=ServiceCreator.create<PlaceService>()
    //创建一个协程函数，调用placeService的searchPlaces以获取数据
    //调用searchPlaces函数，会返回所查询的数据。
    suspend fun searchPlaces(query:String)=placeService.searchPlaces(query).await()
    //定义了一个Call<T>的扩展函数await，也声明为挂起函数，用于将数据返回。
    private suspend fun <T> Call<T>.await():T{

        return suspendCoroutine {
            continuation->enqueue(object:Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body=response.body()
                Log.d("数据","$body")
                //将body作为返回值进行传递
                if (body!=null) continuation.resume(body)
                else continuation.resumeWithException(
                    RuntimeException("response body is null")
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
            })
        }
    }
    //创建一个WeatherService的动态代理对象
    private val weatherService=ServiceCreator.create<WeatherService>()
    suspend fun getDailyWeather(lng:String,lat:String)= weatherService.getDailyWeather(lng, lat).await()
    suspend fun getRealtimeWeather(lng: String,lat: String)= weatherService.getRealtimeWeather(lng, lat).await()
}