package com.comtip.tip.tangran.Control

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.view.WindowManager
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.serverName
import com.comtip.tip.tangran.usePermission
import kotlinx.android.synthetic.main.password_layout.*

/**
 * Created by TipRayong on 17/5/2561 13:47
 * TangRan
 */
fun passwordDialog(activity: Activity){
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.password_layout)
    mDialog.setCancelable(false)

    mDialog.backPasswordBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.confirmPasswordBT.setOnClickListener {
          usePermission =  serverName == mDialog.password01ET.text.toString()

           if(usePermission) {
               customToast(activity,"Grant Permission.")
           } else {
              // Toast.makeText(activity,"Wrong Password !!!",Toast.LENGTH_SHORT).show()
               passwordDialog(activity)
           }
        mDialog.dismiss()
    }

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()

}


