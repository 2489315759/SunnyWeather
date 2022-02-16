package com.example.sunnyweather2.logic.model

class test {
}
fun  text( a:Int, b:Int,block:()->Int):Int{
    return block()
}
fun main(){
    val a=10
    val b=10
    println(text(a,b){a+b})
}