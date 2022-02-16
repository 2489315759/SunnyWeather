package com.example.sunnyweather2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.widget.Toast

fun String.showToast(context: Context,duration:Int=Toast.LENGTH_SHORT){
    Toast.makeText(context,this,duration).show()
}
fun Int.showToast(context: Context,duration: Int=Toast.LENGTH_SHORT){
    Toast.makeText(context,this,duration).show()
}
class SunnyWeatherApplication :Application(){
    companion object{
        //忽略警告。Application的context在整个声明周期中只会有一个实例
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN="BNBRvBveaD2VfHVI"
    }

    override fun onCreate() {
        super.onCreate()
        //获取应用的context
        context=applicationContext
    }
}