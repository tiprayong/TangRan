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
import com.comtip.tip.tangran.*
import com.comtip.tip.tangran.Control.clientCommand
import com.comtip.tip.tangran.Control.customToast
import com.comtip.tip.tangran.TCP.TCPServer
import com.comtip.tip.tangran.TCP.sendOut
import kotlinx.android.synthetic.main.fragment_client.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ClientFragment : Fragment() {
    lateinit var tcpClientSide: TCPServer

    companion object {
        fun newInstance(): ClientFragment {
            val fragment = ClientFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_client, container, false)

        initializeTCPClientSide(activity, fragmentView)

        val clientName: String = "Client : $identity"
        fragmentView.clientNameTV.text = clientName

        fragmentView.clientMenuBT.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.clientMenuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark,null))
            }  else {
                @Suppress("DEPRECATION")
                fragmentView.clientMenuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
            }
            fragmentView.clientMenuBT.setTextColor(Color.WHITE)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.orderingBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary,null))
            }  else {
                @Suppress("DEPRECATION")
                fragmentView.orderingBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }

            fragmentView.orderingBT.setTextColor(Color.BLACK)

            sendOut(identity, serverName, MENU, "Request Menu", serverIPAddress, activity, fragmentView)
            fragmentView.clientLogTV.text =  "Request Menu."
        }

        fragmentView.orderingBT.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.clientMenuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary,null))
            }  else {
                @Suppress("DEPRECATION")
                fragmentView.clientMenuBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimary))
            }
            fragmentView.clientMenuBT.setTextColor(Color.BLACK)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                fragmentView.orderingBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark,null))
            } else {
                @Suppress("DEPRECATION")
                fragmentView.orderingBT.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
            }
            fragmentView.orderingBT.setTextColor(Color.WHITE)

            sendOut(identity, serverName, ALL_ORDER, "Request Orders", serverIPAddress, activity, fragmentView)
            fragmentView.clientLogTV.text = "Request Orders."
        }
        return fragmentView
    }

    private fun initializeTCPClientSide(activity: Activity, view: View) {
        tcpClientSide = TCPServer(TCP_SERVER_PORT)
        tcpClientSide.setOnDataReceivedListener(object : TCPServer.OnDataReceivedListener {
            @SuppressLint("SetTextI18n")
            override fun onDataReceived(message: String, ip: String) {

                //view.clientNameTV.text = message
             if(message.contains(":")) {
                 clientCommand(activity, view, message)
             } else {
                 customToast(activity,"Please Check Server Name or IP address.")
             }
            }
        })
        tcpClientSide.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        tcpClientSide.stop()
    }

}
