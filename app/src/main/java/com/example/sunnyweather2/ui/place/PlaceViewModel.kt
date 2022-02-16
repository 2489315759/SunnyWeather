package com.example.sunnyweather2.ui.place

import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather2.logic.Repository
import com.example.sunnyweather2.logic.model.Place


class PlaceViewModel:ViewModel() {
    //定义了一个可变的LiveData
    private val searchLiveData=MutableLiveData<String>()
    //placeList用于缓存得到的城市数据。
    val placeList=ArrayList<Place>()
    //检测到searchLiveData发生改变后，调用Repository.searchPlaces将query传入。
    val placeLiveData=Transformations.switchMap(searchLiveData){
        query->Repository.searchPlaces(query)
    }
    //将获取到额数据，传递给searchLiveData
    fun searchPlaces(query:String){
        searchLiveData.value=query

    }
    fun savePlace(place: Place)=Repository.savePlace(place)
    fun getSavePlace()=Repository.getSavedPlace()
    fun isPlaceSaved()=Repository.isPlaceSaved()
}