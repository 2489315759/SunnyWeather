package com.example.sunnyweather2.logic.model
//用于封装当日天气与多日天气的数据模型
data class Weather(val realtime:RealtimeResponse.Realtime,val daily:DailyResponse.Daily) {
}