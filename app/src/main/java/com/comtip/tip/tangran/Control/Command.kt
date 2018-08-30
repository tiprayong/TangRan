package com.comtip.tip.tangran.Control

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.comtip.tip.tangran.*
import com.comtip.tip.tangran.ClientPresenter.setupFoodMenuClientListView
import com.comtip.tip.tangran.ClientPresenter.setupOrderClientListView
import com.comtip.tip.tangran.ClientPresenter.setupTypeClientListView
import com.comtip.tip.tangran.ServerPresenter.realTimeAdapter
import com.comtip.tip.tangran.ServerPresenter.setupOrderListView
import com.comtip.tip.tangran.TCP.sendBack
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_server.view.*


/**
 * Created by TipRayong on 19/5/2561 13:03
 * TangRan
 */
@SuppressLint("SetTextI18n")
fun serverCommand(activity: Activity, view: View, message: String, ip: String) {

    // format {command}:{client}|{data}
    val separatorCommand = message.indexOf(':')
    val command = message.substring(0, separatorCommand)
    val separatorClient = message.indexOf('|')
    val client = message.substring(separatorCommand + 1, separatorClient)
    val data: String = message.substring(separatorClient + 1)

    when (command) {
        FOOD_ORDERING -> {   // อัพเดทรายการสั่งอาหาร
            val ordering = convertOrdering(data)
            ordering.orderTime = orderTimeStamp()  //ใช้ timestamp ขของ Server
            orderingDataView.addNewOrdering(ordering)

            if (isOrderingList) {  // ตรวจสอบว่าอยู่หน้า order ListView ไหม (Server Side )
                realTimeAdapter.add(ordering)
            } else {
                Toast.makeText(activity, "New Order !!!", Toast.LENGTH_SHORT).show()
            }
        }

        ALL_ORDER -> {
            val orderData: String = Gson().toJson(orderingDataView.getAllOrdering(client))
            sendBack(identity, client, ALL_ORDER, orderData, ip)
        }

        MENU -> {  // ส่งข้อมูล ประเภทรายการอาหาร กลับไปหา client
            //identity ของ server คือ serverName ของ client
            val menuData: String = Gson().toJson(foodDataView.getMenuType())
            sendBack(identity, client, MENU, menuData, ip)  // clientName is serverName
        }

        FOOD_TYPE -> { // ส่งช้อมูล รายการอาหารตามประเภท กลับไปหา client
            val foodType: String = Gson().toJson(foodDataView.selectMenuByType(data))
            sendBack(identity, client, FOOD_TYPE, foodType, ip)
        }

        CANCEL_ORDERING -> {
            val cancelOrdering = convertOrdering(data)

            orderingDataView.editOrdering(cancelOrdering.primaryKey,
                    cancelOrdering.addition, 0, orderTimeStamp() + " CANCEL")


            val orderData: String = Gson().toJson(orderingDataView.getAllOrdering(client))
            sendBack(identity, client, ALL_ORDER, orderData, ip)

            view.serverLogTV.text = "Cancel " + cancelOrdering.client + "/" + cancelOrdering.food
            if (isOrderingList) {
                mainLvOrderInstance = view.mainLV.onSaveInstanceState()
                setupOrderListView(activity, view)
            } else {
                Toast.makeText(activity, "Cancel Order !!!", Toast.LENGTH_SHORT).show()
            }
        }

        UPDATE_ORDERING -> {
            val editOrdering = convertOrdering(data)
            orderingDataView.editOrdering(editOrdering.primaryKey,
                    editOrdering.addition, editOrdering.amount, orderTimeStamp() + " UPDATE")

            val orderData: String = Gson().toJson(orderingDataView.getAllOrdering(client))
            sendBack(identity, client, ALL_ORDER, orderData, ip)

            view.serverLogTV.text = "Update " + editOrdering.client + "/" + editOrdering.food

            if (isOrderingList) {
                mainLvOrderInstance = view.mainLV.onSaveInstanceState()
                setupOrderListView(activity, view)
            } else {
                Toast.makeText(activity, "Update Order !!!", Toast.LENGTH_SHORT).show()
            }
        }

        else -> Log.i("TangRan Server", "Unknown")

    }

}

fun clientCommand(activity: Activity, view: View, message: String) {
    // format [ command:data ]
    val separator = message.indexOf(':')
    val command = message.substring(0, separator)
    val data: String = message.substring(separator + 1)

    when (command) {
        MENU -> setupTypeClientListView(activity, view, convertTypeMenu(data))
        FOOD_TYPE -> setupFoodMenuClientListView(activity, view, convertFoodMenu(data))
        ALL_ORDER -> setupOrderClientListView(activity, view, convertAllOrder(data))
        else -> customToast(activity, "Please Check Server Name or IP address.")
    }
}

