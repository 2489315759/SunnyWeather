package com.example.sunnyweather2.logic.model

import com.google.gson.annotations.SerializedName
//搜索城市的数据模型
//place的格式为数组
class PlaceResponse(val status: String, val places: List<Place>)

class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)

class Location(val lng: String, val lat: String)
