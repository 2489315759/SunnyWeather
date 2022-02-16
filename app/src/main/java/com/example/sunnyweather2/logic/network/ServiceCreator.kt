package com.example.sunnyweather2.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//retrofit构建器
object ServiceCreator{
    private const val BASE_URL="https://api.caiyunapp.com/"
    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //使用reified关键字将T实例化，定义一个内联函数，当调用create时会返回retrofit.create(serviceClass)
    fun <T>create(serviceClass:Class<T>):T=retrofit.create(serviceClass)
    inline fun <reified T>create():T= create(T::class.java)
}
