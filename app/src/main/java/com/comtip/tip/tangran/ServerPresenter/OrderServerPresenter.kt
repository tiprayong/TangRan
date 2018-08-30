package com.comtip.tip.tangran.ServerPresenter

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
import com.comtip.tip.tangran.Adapter.BillingAdapter
import com.comtip.tip.tangran.Adapter.CheckingOrderAdapter
import com.comtip.tip.tangran.Adapter.OrderAdapter
import com.comtip.tip.tangran.Control.billTimeStamp
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.mainLvOrderInstance
import com.comtip.tip.tangran.orderingDataView
import kotlinx.android.synthetic.main.billing_layout.*
import kotlinx.android.synthetic.main.fragment_server.view.*

/**
 * Created by TipRayong on 10/5/2561 12:45
 * TangRan
 */

lateinit var realTimeAdapter:OrderAdapter


fun setupOrderListView(activity: Activity, view: View) {

    val orderings = orderingDataView.getRealTimeOrdering()
    realTimeAdapter = OrderAdapter(activity,orderings)
    view.mainLV.adapter = realTimeAdapter

    if(mainLvOrderInstance != null){
        view.mainLV.onRestoreInstanceState(mainLvOrderInstance)
        mainLvOrderInstance = null
    }
    view.mainLV.setOnItemClickListener { _, _, i, l ->

        val alertOrder = AlertDialog.Builder(activity)
        alertOrder.setTitle(orderings[i].client)

        alertOrder.setPositiveButton("Billing"){_,_->
            billingDialog(activity, view, orderings[i].client)
        }

        alertOrder.setNegativeButton("Clear Order"){_,_->
            clearOrderDialog(activity, view, orderings[i].client)
        }

        alertOrder.setNeutralButton("Checking"){_,_->
            checkingOrderDialog(activity, view,orderings[i].client)
        }

        val alertView = alertOrder.create()
        alertView.show()

        val billingIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(android.R.drawable.ic_menu_agenda, null)
        } else {
            activity.resources.getDrawable(android.R.drawable.ic_menu_agenda)
        }
        billingIcon.setBounds(0, 0, billingIcon.intrinsicWidth, billingIcon.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_POSITIVE)
                .setCompoundDrawables(billingIcon, null, null, null)

        val clearIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(android.R.drawable.ic_menu_delete, null)
        } else {
            activity.resources.getDrawable(android.R.drawable.ic_menu_delete)
        }
        clearIcon.setBounds(0, 0, clearIcon.intrinsicWidth, clearIcon.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setCompoundDrawables(clearIcon, null, null, null)

        val checkingIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(android.R.drawable.ic_menu_view, null)
        } else {
            activity.resources.getDrawable(android.R.drawable.ic_menu_view)
        }
        checkingIcon.setBounds(0, 0, checkingIcon.intrinsicWidth, checkingIcon.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setCompoundDrawables(checkingIcon, null, null, null)
    }


}

@SuppressLint("SetTextI18n")
fun clearOrderDialog(activity: Activity, view: View, clientName:String) {
    val alertDelete = AlertDialog.Builder(activity)
    alertDelete.setTitle("Clear Orders $clientName ?")

    alertDelete.setPositiveButton("Clear") { _, _ ->
        orderingDataView.deleteOrderingClient(clientName)
        Toast.makeText(activity, "Clear  $clientName", Toast.LENGTH_SHORT).show()
        view.serverLogTV.text = "Clear Orders $clientName"
        setupOrderListView(activity, view)
    }

    alertDelete.setNeutralButton("Clear and Save"){_,_->
        //todo  save log    billing  table  database
    }

    alertDelete.setNegativeButton("BACK") { _, _ ->
        //Cancel
    }

    val alertView = alertDelete.create()
    alertView.show()

    val confirmIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_delete, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_edit)
    }
    confirmIcon.setBounds(0, 0, confirmIcon.intrinsicWidth, confirmIcon.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(confirmIcon, null, null, null)

    val cancelIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_revert, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_revert)
    }
    cancelIcon.setBounds(0, 0, cancelIcon.intrinsicWidth, cancelIcon.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setCompoundDrawables(cancelIcon, null, null, null)
}

/*
fun generateBill (clientName: String):String{

    val clientOrder = orderingDataView.getAllOrdering(clientName)

    var bill:String = ""
    var sum:Double = 0.0

    for (order in clientOrder){
         bill += order.orderTime+ " / "+order.food+
                 " / "+order.addition + " / " +order.price + " / " +order.amount + " = " +(order.amount*order.price) + "\n"

        sum += (order.amount*order.price)
    }

    bill += "Total $sum"

    return bill
}
*/

@SuppressLint("SetTextI18n")
fun billingDialog(activity: Activity, view:View, clientName: String){
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.billing_layout)
    mDialog.setCancelable(false)

    mDialog.billingBT.setOnClickListener {

        Toast.makeText(activity,"Send To Client",Toast.LENGTH_SHORT).show()
        //todo  something  about bill
        mDialog.dismiss()
    }
    
    val clientOrder = orderingDataView.getAllOrdering(clientName)

    mDialog.billingLV.adapter = BillingAdapter(activity,clientOrder)

    var sum:Double = 0.0

    for (order in clientOrder){
        sum += (order.amount*order.price)
    }

    mDialog.billingBT.text = billTimeStamp()+
            "\n"+clientOrder[0].client+
            "\n$sum"+activity.getString(R.string.currency)

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()

}



@SuppressLint("SetTextI18n")
fun checkingOrderDialog(activity: Activity, view:View, clientName: String){
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.billing_layout)
    mDialog.setCancelable(false)

    mDialog.billingBT.text = clientName
    mDialog.billingBT.setOnClickListener {
        mDialog.dismiss()
    }

    val clientOrder = orderingDataView.getAllOrdering(clientName)
    mDialog.billingLV.adapter = CheckingOrderAdapter(activity,clientOrder)

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()

}
