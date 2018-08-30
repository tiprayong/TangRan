package com.comtip.tip.tangran.ClientPresenter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.comtip.tip.tangran.*
import com.comtip.tip.tangran.Adapter.FoodAdapter
import com.comtip.tip.tangran.Adapter.TypeAdapter
import com.comtip.tip.tangran.Control.orderTimeStamp
import com.comtip.tip.tangran.RoomDatabase.Food
import com.comtip.tip.tangran.RoomDatabase.Ordering
import com.comtip.tip.tangran.TCP.sendOut
import com.google.gson.Gson
import kotlinx.android.synthetic.main.food_ordering_layout.*
import kotlinx.android.synthetic.main.fragment_client.view.*


/**
 * Created by TipRayong on 15/5/2561 11:26
 * TangRan
 */

//เมนูประเภทอาหาร
@SuppressLint("SetTextI18n")
fun setupTypeClientListView(activity: Activity, view: View, foodTypes: List<String>) {

    if (foodTypes.isNotEmpty()) {
        val adapter = TypeAdapter(activity, foodTypes)
        view.clientLV.adapter = adapter

        view.clientLV.setOnItemClickListener { _, _, i, l ->

            sendOut(identity, serverName, FOOD_TYPE, foodTypes[i], serverIPAddress, activity, view)
            view.clientLogTV.text = "Request Menu " + foodTypes[i]
        }
    } else {
        view.clientLV.adapter = null
    }
}

//เมนูอาหาร
fun setupFoodMenuClientListView(activity: Activity, view: View, foods: List<Food>) {

    if (foods.isNotEmpty()) {
        val adapter = FoodAdapter(activity, foods)
        view.clientLV.adapter = adapter
        view.clientLV.setOnItemClickListener { _, _, i, l ->

            val alertFood = AlertDialog.Builder(activity)
            alertFood.setTitle(foods[i].name)
            alertFood.setPositiveButton(activity.getString(R.string.ordering)) { _, _ ->
                orderFoodDialog(activity, view, foods[i])
            }

            if (foods[i].mediaUrl.contains("HTTP", true)) {
                alertFood.setNeutralButton(activity.getString(R.string.view_media)) { _, _ ->

                    openWebView(activity, foods[i].name, foods[i].mediaUrl)
                }
            }

            val alertView = alertFood.create()
            alertView.show()

            val orderingIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(android.R.drawable.ic_menu_add, null)
            } else {
                activity.resources.getDrawable(android.R.drawable.ic_menu_add)
            }

            orderingIcon.setBounds(0, 0, orderingIcon.intrinsicWidth, orderingIcon.intrinsicHeight)
            alertView.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setCompoundDrawables(orderingIcon, null, null, null)

            val mediaIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(android.R.drawable.ic_menu_gallery, null)
            } else {
                activity.resources.getDrawable(android.R.drawable.ic_menu_gallery)
            }
            mediaIcon.setBounds(0, 0, mediaIcon.intrinsicWidth, mediaIcon.intrinsicHeight)
            alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setCompoundDrawables(mediaIcon, null, null, null)
        }
    } else {
        view.clientLV.adapter = null
    }
}

// Dialog  สั่งอาหาร
@SuppressLint("SetTextI18n")
fun orderFoodDialog(activity: Activity, view: View, food: Food) {
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.food_ordering_layout)
    mDialog.setCancelable(false)

    mDialog.foodOrderNameTV.text = food.name
    mDialog.foodOrderPriceTV.text = food.price.toString() + activity.getString(R.string.currency)

    mDialog.cancelFoodOrderBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.foodOrderBT.setOnClickListener {
        if (mDialog.foodOrderAmountET.text.toString().isNotEmpty()) {

            val mAmount = mDialog.foodOrderAmountET.text.toString().toInt()
            if (mAmount > 0) {
                val ordering = Ordering(
                        0,
                        identity,
                        food.name,
                        food.type,
                        mDialog.foodOrderAdditionET.text.toString(),
                        mAmount,
                        food.price,
                        orderTimeStamp())

                sendOut(identity, serverName, FOOD_ORDERING,
                        Gson().toJson(ordering), serverIPAddress, activity, view)

                Toast.makeText(activity, activity.getString(R.string.ordering) +
                        food.name, Toast.LENGTH_SHORT).show()

                view.clientLogTV.text = activity.getString(R.string.ordering) + food.name + " " + mAmount

                mDialog.dismiss()
            }
        }
    }

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()
}
