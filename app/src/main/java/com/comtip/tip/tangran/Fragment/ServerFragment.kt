package com.comtip.tip.tangran.Fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.comtip.tip.tangran.Control.getIPAddress
import com.comtip.tip.tangran.Control.serverCommand
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.ServerPresenter.*
import com.comtip.tip.tangran.TCP.TCPServer
import com.comtip.tip.tangran.TCP_SERVER_PORT
import com.comtip.tip.tangran.isOrderingList
import kotlinx.android.synthetic.main.fragment_server.view.*

class ServerFragment : Fragment() {
    lateinit var tcpServerSide: TCPServer

    companion object {
        fun newInstance(): ServerFragment {
            val fragment = ServerFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_server, container, false)

        initializeTCPServerSide(activity, fragmentView)

        val ip: String = activity.getString(R.string.server_ip) + getIPAddress(activity)
        fragmentView.serverIPTV.text = ip

         /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fragmentView.renewFB.backgroundTintList = ColorStateList.valueOf(activity.resources.getColor(R.color.blackBlueFloat, null))
        } else {
            @Suppress("DEPRECATION")
            fragmentView.renewFB.backgroundTintList = ColorStateList.valueOf(activity.resources.getColor(R.color.blackBlueFloat))
        }
          */

        //Client management
        fragmentView.clientBT.setOnClickListener {

            fragmentView.menuBT.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.menuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.menuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            fragmentView.clientBT.setTextColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.clientBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.clientBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
            }

            fragmentView.orderBT.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.orderBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.orderBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            isOrderingList = false

            fragmentView.renewFB.visibility = View.VISIBLE

            fragmentView.renewFB.setOnClickListener {
                updateClientDialog(activity, fragmentView, false)
            }

            setupClientManagementListView(activity, fragmentView)
        }

        //Menu Management
        fragmentView.menuBT.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.menuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.menuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
            }
            fragmentView.menuBT.setTextColor(Color.WHITE)

            fragmentView.clientBT.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.clientBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.clientBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            fragmentView.orderBT.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.orderBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.orderBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            isOrderingList = false

            fragmentView.renewFB.visibility = View.VISIBLE

            fragmentView.renewFB.setOnClickListener {
                updateFoodDialog(activity, fragmentView, false)
            }

            setupTypeListView(activity, fragmentView)
        }

        //Order Management
        fragmentView.orderBT.setOnClickListener {

            fragmentView.menuBT.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.menuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.menuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            fragmentView.clientBT.setTextColor(Color.BLACK)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.clientBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.clientBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            fragmentView.orderBT.setTextColor(Color.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.orderBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark, null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.orderBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
            }

            isOrderingList = true

            fragmentView.renewFB.visibility = View.INVISIBLE
            setupOrderListView(activity, fragmentView)
        }
        return fragmentView
    }


    private fun initializeTCPServerSide(activity: Activity, view: View) {
        tcpServerSide = TCPServer(TCP_SERVER_PORT)
        tcpServerSide.setOnDataReceivedListener(object : TCPServer.OnDataReceivedListener {
            @SuppressLint("SetTextI18n")
            override fun onDataReceived(message: String, ip: String) {

                // view.serverIPTV.text = message  // just test  can be delete

                serverCommand(activity, view, message, ip)
            }
        })

        tcpServerSide.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        tcpServerSide.stop()
    }
}
