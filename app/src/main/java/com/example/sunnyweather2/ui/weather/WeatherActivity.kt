package com.example.sunnyweather2.ui.weather

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView

import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather2.R
import com.example.sunnyweather2.logic.model.Weather
import com.example.sunnyweather2.logic.model.getSky
import com.example.sunnyweather2.showToast
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.forecast_item.view.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //获取系统的decorView
        val decorView=window.decorView
        //将activity布局设置到状态栏
        decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //将状态栏设置为透明
        window.statusBarColor= Color.TRANSPARENT

        setContentView(R.layout.activity_weather)
        //如果locatonLng为空值，通过Intent获取传递的locationLng参数，赋值给locationLng
        if (viewModel.locationLng.isEmpty()){
            viewModel.locationLng=intent.getStringExtra("location_lng")?:""
        }
        if (viewModel.locationLat.isEmpty()){
            viewModel.locationLat=intent.getStringExtra("location_lat")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("place_name")?:""
        }
        //检测weatherLiveData，将封装好的值赋值给weather，如果weather不为空就将数据传入showWeatherInfo()
        viewModel.weatherLiveData.observe(this, Observer {
            result-> val weather=result.getOrNull()
            if (weather!=null){
                Log.d("测试","$weather")
                showWeatherInfo(weather)
            }else{
                "无法成功获取天气".showToast(this)
                result.exceptionOrNull()?.printStackTrace()
            }
            //设置刷新的状态
            swipeRefresh.isRefreshing=false
        })
        //设置刷新的颜色
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        navBtn.setOnClickListener {
            //点击按钮时打开侧边菜单
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {
            }
            override fun onDrawerOpened(drawerView: View) {
            }
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }
            override fun onDrawerClosed(drawerView: View) {
                //当侧边菜单关闭时，关闭输入键盘
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })

    }
    private fun showWeatherInfo(weather:Weather){
        //将viewModel中的数据取出赋值给ui控件
        placeName.text=viewModel.placeName
        val realtime=weather.realtime
        val daily=weather.daily
        //设置气温文本
        val currentTempText="${realtime.temperature.toInt()}°C"
        currentTemp.text=currentTempText
        //设置天气文本
        currentSky.text= getSky(realtime.skycon).info
        //设置空气指数文本
        val currentPM25Text="空气指数${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text=currentPM25Text
        //设置当前天气的背景图
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        //移除其他子视图
        forecastLayout.removeAllViews()
        //获取多日天气的长度
        val days=daily.skycon.size
        //循环输入
        for (i in 0 until days){
            val skycon=daily.skycon[i]
            val temperature=daily.temperature[i]
            val view=LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val dateInfo=view.dateInfo as TextView
            val skyIcon=view.skyIcon as ImageView
            val skyInfo=view.skyInfo as TextView
            val temperatureInfo=view.temperatureInfo
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            //设置时间
            dateInfo.text=simpleDateFormat.format(skycon.date)
            val sky= getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text=sky.info
            //设置气温
            val tempText="${temperature.min.toInt()}~${temperature.max.toInt()}°C"
            temperatureInfo.text=tempText
            //将子视图添加到主视图中
            forecastLayout.addView(view)

        }
        val lifeIndex=daily.lifeIndex
        //设置生活指数
        coldRiskText.text=lifeIndex.coldRisk[0].desc
        dressingText.text=lifeIndex.dressing[0].desc
        ultravioletText.text=lifeIndex.ultraviolet[0].desc
        carWashingText.text=lifeIndex.carWashing[0].desc
        //将weatherLayout设置为可见
        weatherLayout.visibility=View.VISIBLE
    }
    fun refreshWeather(){
        //调用refreshWeather()获取数据
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
        swipeRefresh.isRefreshing=true
    }
}