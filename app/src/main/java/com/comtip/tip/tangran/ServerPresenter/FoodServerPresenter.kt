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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.comtip.tip.tangran.Adapter.FoodAdapter
import com.comtip.tip.tangran.Adapter.TypeAdapter
import com.comtip.tip.tangran.Control.customToast
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.RoomDatabase.Food
import com.comtip.tip.tangran.foodDataView
import com.comtip.tip.tangran.openWebView
import kotlinx.android.synthetic.main.food_layout.*
import kotlinx.android.synthetic.main.fragment_server.view.*

/**
 * Created by TipRayong on 9/5/2561 12:56
 * TangRan
 */
fun setupTypeListView(activity: Activity, view: View) {
    val foodTypes = foodDataView.getMenuType()
    if (foodTypes.isNotEmpty()) {
        val adapter = TypeAdapter(activity, foodTypes)
        view.mainLV.adapter = adapter
        view.mainLV.setOnItemClickListener { _, _, i, l ->
            setupFoodMenuListView(activity, view, foodTypes[i])
        }
    } else {
        view.mainLV.adapter = null
    }
}

fun setupFoodMenuListView(activity: Activity, view: View, type: String) {
    val foods = foodDataView.selectMenuByType(type)

    if (foods.isNotEmpty()) {
        val adapter = FoodAdapter(activity, foods)
        view.mainLV.adapter = adapter
        view.mainLV.setOnItemClickListener { _, _, i, l ->

            val alertFood = AlertDialog.Builder(activity)
            alertFood.setTitle(foods[i].name)
            alertFood.setPositiveButton("Edit") { _, _ ->
                updateFoodDialog(activity, view, true, foods[i])
            }

            alertFood.setNegativeButton("Delete") { _, _ ->
                deleteFoodDialog(activity, view, foods[i])
            }

            alertFood.setNeutralButton("Media") { _, _ ->

                if (foods[i].mediaUrl.contains("HTTP", true)) {
                    // openChromeCustomTab(activity, foods[i].mediaUrl)
                    openWebView(activity, foods[i].name, foods[i].mediaUrl)
                } else {
                    customToast(activity, "Can't Open !!!")
                }
            }

            val alertView = alertFood.create()
            alertView.show()

            val editIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(android.R.drawable.ic_menu_edit, null)
            } else {
                activity.resources.getDrawable(android.R.drawable.ic_menu_edit)
            }
            editIcon.setBounds(0, 0, editIcon.intrinsicWidth, editIcon.intrinsicHeight)
            alertView.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setCompoundDrawables(editIcon, null, null, null)

            val deleteIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(android.R.drawable.ic_menu_delete, null)
            } else {
                activity.resources.getDrawable(android.R.drawable.ic_menu_delete)
            }
            deleteIcon.setBounds(0, 0, deleteIcon.intrinsicWidth, deleteIcon.intrinsicHeight)
            alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setCompoundDrawables(deleteIcon, null, null, null)

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
        view.mainLV.adapter = null
    }
}


@SuppressLint("SetTextI18n")
fun updateFoodDialog(activity: Activity, view: View, isEdit: Boolean, food: Food = Food(0, "", "", 0.0, "")) {
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.food_layout)
    mDialog.setCancelable(false)


    val typeList = foodDataView.getMenuType()
    if(typeList.isNotEmpty()) {

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, typeList)
        mDialog.typeFoodSP.adapter = adapter

            mDialog.typeFoodSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        mDialog.typeFoodET.setText(typeList[p2])
                }
            }

        var currentIndex = 0
        for (tl in typeList){
            if(tl == food.type){
                  mDialog.typeFoodSP.setSelection(currentIndex)
            }
            currentIndex += 1
        }

    }


    if (isEdit) {
        mDialog.nameFoodET.setText(food.name)
        mDialog.priceFoodET.setText(food.price.toString())
        mDialog.mediaFoodET.setText(food.mediaUrl)
        mDialog.updateFoodBT.text = "UPDATE"
    } else {
        mDialog.nameFoodET.hint = "Food Name"
        mDialog.typeFoodET.hint = "Food Type"
        mDialog.typeFoodET.setText("")
        mDialog.priceFoodET.hint = "Price"
        mDialog.mediaFoodET.hint = "Media URL"
        mDialog.updateFoodBT.text = "ADD"
    }


    mDialog.cancelFoodBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.updateFoodBT.setOnClickListener {
        if (mDialog.nameFoodET.text.isNotEmpty()
                && mDialog.typeFoodET.text.isNotEmpty()
                && mDialog.priceFoodET.text.isNotEmpty()
               // && mDialog.mediaFoodET.text.isNotEmpty()
        ) {

            if (isEdit) {
                foodDataView.editMenu(food.primaryKey
                        , mDialog.nameFoodET.text.toString()
                        , mDialog.typeFoodET.text.toString()
                        , mDialog.priceFoodET.text.toString().toDouble()
                        , mDialog.mediaFoodET.text.toString()
                )
            } else {
                food.name = mDialog.nameFoodET.text.toString()
                food.type = mDialog.typeFoodET.text.toString()
                food.price = mDialog.priceFoodET.text.toString().toDouble()
                food.mediaUrl = mDialog.mediaFoodET.text.toString()

                foodDataView.addNewMenu(food)
                Toast.makeText(activity, "Add Menu " + food.name, Toast.LENGTH_SHORT).show()
                view.serverLogTV.text = "Add Menu " + food.name
            }
            setupFoodMenuListView(activity, view, food.type)
            mDialog.dismiss()

        } else {
            Toast.makeText(activity, "Data Empty", Toast.LENGTH_SHORT).show()
        }
    }

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()
}

@SuppressLint("SetTextI18n")
fun deleteFoodDialog(activity: Activity, view: View, food: Food) {
    val alertDelete = AlertDialog.Builder(activity)
    alertDelete.setTitle("Delete " + food.name + " ?")

    alertDelete.setPositiveButton("Confirm") { _, _ ->
        foodDataView.deleteMenu(food.primaryKey)
        Toast.makeText(activity, "Delete " + food.name, Toast.LENGTH_SHORT).show()
        view.serverLogTV.text = "Delete " + food.name
        setupFoodMenuListView(activity, view, food.type)
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
