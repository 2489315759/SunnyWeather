package com.example.sunnyweather2.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather2.logic.Repository.searchPlaces
import com.example.sunnyweather2.logic.dao.PlaceDao
import com.example.sunnyweather2.logic.model.Place
import com.example.sunnyweather2.logic.model.Weather
import com.example.sunnyweather2.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

//仓库层：主要功能判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获得的数据返回给调用方。
object Repository {
    //liveData()函数会自动构建一个LiveData对象，在他的代码块中提供一个挂起函数的上下文。
    //使用Result.success方法来包装获取到的数据
    //使用Result.success方法来包装一个异常信息。
    //emit()方法，将LiveData设置为给定的值。
    //将liveData函数的线程类型都指定为了Dispatchers.IO，这样所有的liveData都运行在了子线程中。
    fun searchPlaces(query:String)= fire(Dispatchers.IO) {
            val placeResponse= SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status=="ok"){
                val places=placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
    }
    //定义了一个refershWeather用于同时获取两组数据
    fun refreshWeather(lng:String,lat:String,placeName:String)= fire(Dispatchers.IO) {
        //创建一个协程域，corutineScope:不会阻塞当前线程，会等作用域当中的所有协程都执行完才会继续向下进行
            coroutineScope {
                val deferredRealtime=async {
                    SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
                }
                val deferredDaily=async {
                    SunnyWeatherNetwork.getDailyWeather(lng, lat)
                }
                //当两个并发函数都获得数据，才会继续线程，await会一直阻塞线程直到得到响应
                val realtimeResponse=deferredRealtime.await()
                val dailyResponse=deferredDaily.await()

                if (realtimeResponse.status=="ok"&&dailyResponse.status=="ok"){
                    //将两个数据包中的realtime与daily传入到Weather数据类中
                    val weather=Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    //将weather打包
                    Log.d("测试","响应=${weather}")
                    Result.success(weather)
                }else{
                    //打包失败信息
                    Result.failure(
                        RuntimeException(
                            "realtime response status is ${realtimeResponse.status}"+
                            "daily response status is ${dailyResponse.status}"
                        )
                    )
                }
            }
        }
    fun savePlace(place:Place)=PlaceDao.savePlace(place)
    fun getSavedPlace()=PlaceDao.getSavedPlace()
    fun isPlaceSaved()=PlaceDao.isPlaceSaved()
    }
    //在()前加上suspend是因为，在liveData代码块中是拥有挂起函数的上下文的，但在lambda表达式中没有，所以将其声明为suspend函数
    private fun <T> fire(context: CoroutineContext,block:suspend ()->Result<T>)=
        liveData<Result<T>>(context) {
            val result=try {
                block()
            }catch (e:Exception){
                Result.failure<T>(e)
            }
            emit(result)
        }
