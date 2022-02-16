package com.example.sunnyweather2.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather2.SunnyWeatherApplication
import com.example.sunnyweather2.logic.model.Place
import com.google.gson.Gson

object PlaceDao {
    fun savePlace(place: Place){
        //将place转换为Json字符串
        sharedPreferences().edit {
            putString("place",Gson().toJson(place))
        }
    }
    fun getSavedPlace():Place{
        //读取再将place解析为Place对象
        val placeJson= sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }
    //判断place是否被储存
    fun isPlaceSaved()= sharedPreferences().contains("place")
    private fun sharedPreferences()=
        SunnyWeatherApplication.context.getSharedPreferences("sunny_weather",Context.MODE_PRIVATE)
}