package com.comtip.tip.tangran.Control

import com.comtip.tip.tangran.RoomDatabase.Food
import com.comtip.tip.tangran.RoomDatabase.Ordering
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by TipRayong on 15/5/2561 11:23
 * TangRan
 */

fun convertTypeMenu(menuData:String):List<String>{
    val listType = object : TypeToken<List<String>>(){}.type
    return  Gson().fromJson(menuData,listType)
}

fun convertFoodMenu(foodData:String):List<Food>{
    val listType = object : TypeToken<List<Food>>(){}.type
    return Gson().fromJson(foodData,listType)
}

fun convertAllOrder(orderData:String):List<Ordering>{
    val listType = object : TypeToken<List<Ordering>>(){}.type
    return Gson().fromJson(orderData,listType)
}

fun convertOrdering (ordering:String):Ordering{
    val orderingType = object  : TypeToken<Ordering>(){}.type
    return Gson().fromJson(ordering,orderingType)
}