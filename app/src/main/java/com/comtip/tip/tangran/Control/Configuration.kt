package com.comtip.tip.tangran.Control

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.net.wifi.WifiManager
import android.os.Build
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.comtip.tip.tangran.*
import kotlinx.android.synthetic.main.identity_layout.*
import kotlinx.android.synthetic.main.server_name_layout.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by TipRayong on 13/5/2561 11:31
 * TangRan
 */
fun ipConfigDialog(activity: Activity) {
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.server_name_layout)
    mDialog.setCancelable(false)

    val ip: String = activity.getString(R.string.deviece_ip_address) + getIPAddress(activity)
    mDialog.deviceAddressTV.text = ip

    mDialog.cancelAddressBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.serverAddressET.setText(serverIPAddress)
    mDialog.serverNameET.setText(serverName)

    //todo warning before save
    mDialog.setAddressBT.setOnClickListener {
        if ((mDialog.serverAddressET.text.toString().isNotEmpty()) &&
                (mDialog.serverNameET.text.toString().isNotEmpty())
        ) {
            serverIPAddress = mDialog.serverAddressET.text.toString()
            serverName = mDialog.serverNameET.text.toString()
            Toast.makeText(activity, "$serverName : $serverIPAddress", Toast.LENGTH_SHORT).show()
            mDialog.dismiss()
        } else {
            Toast.makeText(activity, "Empty!!!", Toast.LENGTH_SHORT).show()
        }
    }
    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()
}

fun identityConfigDialog(activity: Activity) {
    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.identity_layout)
    mDialog.setCancelable(false)

    mDialog.cancelIdentityBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.restaurantET.setText(restaurant)
    mDialog.setIdentityET.setText(identity)
    mDialog.setPortET.setText(TCP_SERVER_PORT.toString())

    mDialog.setIdentityBT.setOnClickListener {
        if (mDialog.setIdentityET.text.toString().isNotEmpty()
                && mDialog.setPortET.text.toString().isNotEmpty()
                && mDialog.restaurantET.text.toString().isNotEmpty()
        ) {
            val mPort = mDialog.setPortET.text.toString().toInt()

            if ((mPort > 49151) && (mPort < 65536)) {   //TCP PORT RANGE 49152–65535
                restaurant = mDialog.restaurantET.text.toString()
                identity = mDialog.setIdentityET.text.toString()
                TCP_SERVER_PORT = mPort
                customToast(activity,"Please Re-Open Application.")
                mDialog.dismiss()
            } else {

                customToast(activity, "Port must be in range 49152 – 65535")
            }

        } else {
            customToast(activity, "Please Check !!!")
        }
    }
    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()
}

fun loadShared(context: Context) {
    val shared = context.getSharedPreferences("status", 0)
    serverIPAddress = shared.getString("serverIPAddress", "0.0.0.0");
    isServer = shared.getBoolean("isServer", false)
    identity = shared.getString("identity", "")
    serverName = shared.getString("serverName", "")
    TCP_SERVER_PORT = shared.getInt("TCP_SERVER_PORT", 65534)
    language = shared.getString("language", "")
    restaurant = shared.getString("restaurant","TangRan")
    textSize = shared.getFloat("textSize",14f)
}

fun saveShared(context: Context) {
    val shared = context.getSharedPreferences("status", 0)
    val editor = shared.edit();
    editor.putBoolean("isServer", isServer)
    editor.putString("serverIPAddress", serverIPAddress)
    editor.putString("identity", identity)
    editor.putString("serverName", serverName)
    editor.putInt("TCP_SERVER_PORT", TCP_SERVER_PORT)
    editor.putString("language", language)
    editor.putString("restaurant", restaurant)
    editor.putFloat("textSize", textSize)
    editor.apply()
}


@Suppress("DEPRECATION")
fun setLanguage(activity: Activity, lang: String) {
    val locale: Locale = if (lang.isEmpty()) {
        Locale.getDefault()
    } else {
        Locale(lang)
    }

    Locale.setDefault(locale)
    val config = Configuration()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocale(locale)
        activity.applicationContext.createConfigurationContext(config)
    } else {
        config.locale = locale
        activity.baseContext.resources.updateConfiguration(
                config, activity.baseContext.resources.displayMetrics
        )
    }
}

fun getIPAddress(context: Context): String {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo
    val ipAddress = wifiInfo.ipAddress
    var ipString = "" + (ipAddress and 0xFF) + "." +
            ((ipAddress shr 8) and 0xFF) + "." +
            ((ipAddress shr 16) and 0xFF) + "." +
            ((ipAddress shr 24) and 0xFF)

    if (ipString == "0.0.0.0") {
        ipString = "Lost connections !!!"
    }
    return ipString
}

@SuppressLint("SimpleDateFormat")
fun orderTimeStamp(): String {
    val formatter = SimpleDateFormat("HH:mm:ss")
    val date = Calendar.getInstance().time
    return formatter.format(date)
}


@SuppressLint("SimpleDateFormat")
fun billTimeStamp(): String {
    val formatter = SimpleDateFormat("yyyyMMdd-HH:mm:ss:SSS")
    val date = Calendar.getInstance().time
    return formatter.format(date)
}