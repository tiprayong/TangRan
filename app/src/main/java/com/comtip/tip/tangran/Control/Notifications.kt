package com.comtip.tip.tangran.Control

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.support.design.widget.Snackbar
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.comtip.tip.tangran.R

/**
 * Created by TipRayong on 2/6/2561 14:04
 * TangRan
 */
//Custom SnackBar
lateinit var  snackBar: Snackbar

fun setSnack(activity: Activity, fragmentView: View, message:String){
    snackBar = Snackbar.make(fragmentView,message, Snackbar.LENGTH_INDEFINITE)
    /*
   .setAction("✘"){
       snackBar.dismiss()
   }.setActionTextColor(Color.RED)
   */

    val view = snackBar.view

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        view.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark,null))
    }  else {
        @Suppress("DEPRECATION")
        view.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
    }

    val textView = view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
    textView.setTextColor(Color.WHITE)
    textView.gravity = Gravity.CENTER_VERTICAL or Gravity.CLIP_HORIZONTAL

    val textAction = view.findViewById<TextView>(android.support.design.R.id.snackbar_action)

    val iconImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(R.mipmap.ic_launcher,null)
    } else {
        @Suppress("DEPRECATION")
        activity.resources.getDrawable(R.mipmap.ic_launcher)
    }
    iconImage.setBounds(0, 0, iconImage.intrinsicWidth, iconImage.intrinsicHeight);
    textAction.setCompoundDrawables(null,null,iconImage,null)
    snackBar.show()
}



fun customToast (activity: Activity, message:String){

    val toast = Toast.makeText(activity,message, Toast.LENGTH_SHORT)
    val toastView =  toast.view
    val toastMessage = toastView.findViewById<TextView>(android.R.id.message)
    toastMessage.textSize = 24F
    toastMessage.setTextColor(Color.WHITE)
    toastMessage.setBackgroundColor(Color.TRANSPARENT)
    /*
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        toastMessage.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark,null))
    }  else {
        @Suppress("DEPRECATION")
        toastMessage.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
    }
    */

    toastMessage.gravity = Gravity.TOP
    toast.show()
}