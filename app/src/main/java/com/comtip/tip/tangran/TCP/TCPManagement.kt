package com.comtip.tip.tangran.TCP

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.comtip.tip.tangran.Control.setSnack
import com.comtip.tip.tangran.Control.snackBar
import com.comtip.tip.tangran.TCP_SERVER_PORT
import com.comtip.tip.tangran.clientDataView
import com.comtip.tip.tangran.identity
import com.comtip.tip.tangran.isServer
import kotlinx.android.synthetic.main.fragment_client.view.*
import kotlinx.android.synthetic.main.fragment_server.view.*

/**
 * Created by TipRayong on 14/5/2561 10:47
 * TangRan
 */

fun checkAuthorize(user:String,password:String):Boolean{

    if(clientDataView.isClientAvailable(user)){
        if(password == identity){ // identity คือ serverName ของเ client
            return true
        }
    }
    return false
}


fun sendBack(server:String,client: String,command:String,data:String,ip:String){
    TCPClient.send(server, client,command,data,ip,TCP_SERVER_PORT,object :TCPClient.SendCallback{
        override fun onSuccess(tag: String?) {
            Log.i("TangRan","Success$tag")
        }
        override fun onFailed(tag: String?) {
            Log.i("TangRan","Fail$tag")    //Todo  Notification
        }
    },"ServerSide")
}


fun sendOut(user:String, password:String, command:String, data:String, ip:String, activity: Activity, view: View){
    if(!isServer){
        setSnack(activity,view,"Please Wait ...");
    }

    TCPClient.send(user,password,command,data,ip,TCP_SERVER_PORT,object :TCPClient.SendCallback{
        @SuppressLint("SetTextI18n")
        override fun onSuccess(tag: String?) {
            if(isServer) {
                view.serverLogTV.text = "Success_$tag"
            }else {
                snackBar.dismiss()
                view.clientLogTV.text = "Success_$tag"
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onFailed(tag: String?) {
            if(isServer) {
                view.serverLogTV.text = "Fail_$tag"

            }else {

                snackBar.dismiss()
                view.clientLogTV.text = "Fail_$tag"

                val alertFail = AlertDialog.Builder(activity)
                alertFail.setTitle(command+"Send Failed")

                alertFail.setPositiveButton("Resend"){_,_->
                    sendOut(user, password, command, data, ip,activity,view)
                }

                alertFail.setNegativeButton("Cancel"){_,_->
                    //back
                }
                alertFail.create().show()
            }
        }
    },command)
}

