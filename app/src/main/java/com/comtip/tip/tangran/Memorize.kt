package com.comtip.tip.tangran

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import com.comtip.tip.tangran.RoomDatabase.BillingDataView
import com.comtip.tip.tangran.RoomDatabase.ClientDataView
import com.comtip.tip.tangran.RoomDatabase.FoodDataView
import com.comtip.tip.tangran.RoomDatabase.OrderingDataView
import com.comtip.tip.tangran.WebPresenter.ActivityWebView

/**
 * Created by TipRayong on 7/5/2561 11:36
 * TangRan
 */

lateinit var billingDataView: BillingDataView
lateinit var clientDataView: ClientDataView
lateinit var foodDataView: FoodDataView
lateinit var orderingDataView: OrderingDataView


var isServer: Boolean = true
var serverIPAddress: String = "0.0.0.0"
var serverName = ""
var identity = ""
var restaurant = "TangRan"
var TCP_SERVER_PORT: Int = 65534   //TCP PORT RANGE 49152–65535

var isOrderingList: Boolean = false

var usePermission = false

var language = ""
//Command
val FOOD_TYPE = "FOOD_TYPE"
val FOOD_ORDERING = "FOOD_ORDERING"
val ALL_ORDER = "ALL_ORDER"
val MENU = "MENU"
val CANCEL_ORDERING = "CANCEL_ORDERING"
val UPDATE_ORDERING = "UPDATE_ORDERING"


// Instance Order ListView (Server Side Only)
var mainLvOrderInstance: Parcelable? = null

// TextSize
var  textSize = 14f

fun openWebView(activity: Activity, name: String, mediaUrl: String) {
    val intent = Intent(activity, ActivityWebView::class.java)
    intent.putExtra("name", name)
    intent.putExtra("mediaUrl", mediaUrl)
    activity.startActivity(intent)
}