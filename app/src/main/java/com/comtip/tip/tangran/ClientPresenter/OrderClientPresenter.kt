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
import com.comtip.tip.tangran.Adapter.BillingAdapter
import com.comtip.tip.tangran.Adapter.OrderAdapter
import com.comtip.tip.tangran.Control.customToast
import com.comtip.tip.tangran.Control.orderTimeStamp
import com.comtip.tip.tangran.RoomDatabase.Ordering
import com.comtip.tip.tangran.TCP.sendOut
import com.google.gson.Gson
import kotlinx.android.synthetic.main.billing_layout.*
import kotlinx.android.synthetic.main.food_ordering_layout.*
import kotlinx.android.synthetic.main.fragment_client.view.*

@SuppressLint("SetTextI18n")
        /**
         * Created by TipRayong on 16/5/2561 12:57
         * TangRan
         */

//  รายการอาหารที่สั่งไว้ (Client Side)
fun setupOrderClientListView(activity: Activity, view: View, orderings: List<Ordering>) {

    val adapter = OrderAdapter(activity, orderings)
    view.clientLV.adapter = adapter

    view.clientLV.setOnItemClickListener { _, _, i, l ->

        val alertOrder = AlertDialog.Builder(activity)
        alertOrder.setTitle(orderings[i].client)

        alertOrder.setPositiveButton(activity.getString(R.string.check_bill)) { _, _ ->
            calcBillDialog(activity, view, orderings)
        }

        alertOrder.setNegativeButton(activity.getString(R.string.edit_order)) { _, _ ->
            if (orderings[i].orderTime.contains("CANCEL")) {
                customToast(activity, "ORDER CANCEL")
            } else {
                editOrderFoodDialog(activity, view, orderings[i])
            }
        }

        alertOrder.setNeutralButton(activity.getString(R.string.cancel_order)) { _, _ ->

            if (orderings[i].orderTime.contains("CANCEL")) {
                customToast(activity, "ORDER CANCEL")
            } else {
                val alertCancelOrder = AlertDialog.Builder(activity)
                alertCancelOrder.setTitle(activity.getString(R.string.cancel_order) + orderings[i].food)

                alertCancelOrder.setPositiveButton("✔") { _, _ ->
                    sendOut(identity, serverName, CANCEL_ORDERING,
                            Gson().toJson(orderings[i]), serverIPAddress, activity, view)

                    view.clientLogTV.text = "CANCEL " + orderings[i].food + " " + orderings[i].amount
                }

                alertCancelOrder.setNegativeButton("✘") { _, _ -> }
                alertCancelOrder.create().show()
            }
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

        val editIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(android.R.drawable.ic_menu_edit, null)
        } else {
            activity.resources.getDrawable(android.R.drawable.ic_menu_edit)
        }
        editIcon.setBounds(0, 0, editIcon.intrinsicWidth, editIcon.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setCompoundDrawables(editIcon, null, null, null)

        val clearIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.resources.getDrawable(android.R.drawable.ic_menu_delete, null)
        } else {
            activity.resources.getDrawable(android.R.drawable.ic_menu_delete)
        }
        clearIcon.setBounds(0, 0, clearIcon.intrinsicWidth, clearIcon.intrinsicHeight)
        alertView.getButton(DialogInterface.BUTTON_NEUTRAL)
                .setCompoundDrawables(clearIcon, null, null, null)
    }
}

// คำนวนค่าอาหารและเรียกเก็บบิล
@SuppressLint("SetTextI18n")
fun calcBillDialog(activity: Activity, view: View, orderings: List<Ordering>) {
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.billing_layout)
    mDialog.setCancelable(false)

    // Todo  Send Request Check BIll
    mDialog.billingBT.setOnClickListener {
        Toast.makeText(activity, "Send Request Check Bill", Toast.LENGTH_SHORT).show()
        view.clientLogTV.text = "Send Request Check Bill"
        mDialog.dismiss()
    }

    mDialog.billingLV.adapter = BillingAdapter(activity, orderings)

    var sum: Double = 0.0
    for (ordering in orderings) {
        sum += (ordering.amount * ordering.price)
    }

    mDialog.billingBT.text = orderings[0].client + "\n$sum " + activity.getString(R.string.currency)

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()
}

//แก้ไขรายการอาหารที่สั่งไปแล้ว
@SuppressLint("SetTextI18n")
fun editOrderFoodDialog(activity: Activity, view: View, ordering: Ordering) {
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.food_ordering_layout)
    mDialog.setCancelable(false)

    mDialog.foodOrderNameTV.text = ordering.food
    mDialog.foodOrderPriceTV.text = ordering.price.toString() + activity.getString(R.string.currency)

    mDialog.cancelFoodOrderBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.foodOrderAmountET.setText(ordering.amount.toString())
    mDialog.foodOrderAdditionET.setText(ordering.addition)

    mDialog.foodOrderBT.setOnClickListener {
        if (mDialog.foodOrderAmountET.text.toString().isNotEmpty()) {

            val mAmount = mDialog.foodOrderAmountET.text.toString().toInt()
            if (mAmount > 0) {

                val editOrdering = Ordering(
                        ordering.primaryKey,
                        ordering.client,
                        ordering.food,
                        ordering.type,
                        mDialog.foodOrderAdditionET.text.toString(),
                        mAmount,
                        ordering.price,
                        orderTimeStamp())

                sendOut(identity, serverName, UPDATE_ORDERING,
                        Gson().toJson(editOrdering), serverIPAddress, activity, view)

                Toast.makeText(activity, activity.getString(R.string.edit_order) + ordering.food, Toast.LENGTH_SHORT).show()
                view.clientLogTV.text = activity.getString(R.string.edit_order) + ordering.food + " " + ordering.amount

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
