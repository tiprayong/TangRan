package com.comtip.tip.tangran.TCP

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import com.comtip.tip.tangran.identity
import com.comtip.tip.tangran.isServer
import com.comtip.tip.tangran.serverName
import java.io.*
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException

/**
 * Created by TipRayong on 14/5/2561 10:45
 * TangRan
 */
class TCPServer(port: Int) {

    private var isRunning = false
    private var service: TCPService? = null
    var targetInetAddress: InetAddress? = null
        private set

    private var port = 65534  //Default Port
    private var isConnected = false

    private var mDataReceivedListener: OnDataReceivedListener? = null

    init {
        this.port = port
    }

    fun start() {
        if (!this.isRunning) {
            this.service = TCPService(port)
            this.service!!.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)
        }
    }

    fun stop() {
        if (this.isRunning) {
            this.service!!.killTask()
            this.isRunning = false
        }
    }

    fun onMessageIncoming(message: String, ip: String) {
        this.mDataReceivedListener!!.onDataReceived(message, ip)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class TCPService(private val port: Int) : AsyncTask<Void, Void, Void>() {
        private var ss: ServerSocket? = null
        private var TASK_STATE: Boolean? = true

        fun killTask() {
            TASK_STATE = false
        }

        override fun doInBackground(vararg params: Void): Void? {
            var s: Socket? = null
            while (this.TASK_STATE!!) {
                try {
                    ss = ServerSocket(port)
                    ss!!.soTimeout = 1000
                    s = ss!!.accept()

                    targetInetAddress = s!!.inetAddress
                    isConnected = true
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                while (TASK_STATE!! && isConnected && s != null) {
                    try {
                        s.soTimeout = 1000

                        //DataPackage {USER}:{SecretCode}|{COMMAND}]{DATA}
                        val incomingMsg = BufferedReader(InputStreamReader(s.getInputStream())).readLine()
                        val separatorClient = incomingMsg!!.indexOf(':')
                        val separatorSecretCode = incomingMsg.indexOf('|')
                        val separatorCommandType = incomingMsg.indexOf(']')

                        //User
                        val client = if (separatorClient == -1) {
                            ""
                        } else {
                            incomingMsg.substring(0, separatorClient)
                        }

                        //Secret tCode(Server Name or Client Name)
                        val password = if (separatorSecretCode == -1) {
                            ""
                        } else {
                            incomingMsg.substring(separatorClient + 1, separatorSecretCode)
                        }

                        //Command
                        val command = if (separatorCommandType == -1) {
                            ""
                        } else {
                            incomingMsg.substring(separatorSecretCode + 1, separatorCommandType)
                        }

                        //Data
                        val messageData = if (separatorCommandType == -1) {
                            incomingMsg
                        } else {
                            incomingMsg.substring(separatorCommandType + 1)
                        }

                        var dataPackage: String = ""

                        if (isServer) {
                            //====Server Control
                            dataPackage = if (client.isNotEmpty() && password.isNotEmpty()) {
                                if (checkAuthorize(client, password)) {
                                    "$command:$client|$messageData"
                                } else {
                                    "Unauthorized"
                                }
                            } else {
                                "Not Allow"
                            }
                        } else {
                            //===Client Control
                            if (client.isNotEmpty() && password.isNotEmpty()) {
                                if ((client == serverName) && (password == identity)) { //identity = clientName
                                    dataPackage = "$command:$messageData"
                                }
                            }
                        }

                        if (mDataReceivedListener != null && incomingMsg.length > 0) {
                            Handler(Looper.getMainLooper()).post(Runnable {
                                mDataReceivedListener!!.onDataReceived(dataPackage,
                                        targetInetAddress!!.hostAddress)
                            })
                        }

                        val out = BufferedWriter(OutputStreamWriter(s.getOutputStream()))
                        val outgoingMsg = "%OK%" + System.getProperty("line.separator")
                        out.write(outgoingMsg)
                        out.flush()

                    } catch (e: NullPointerException) {
                        isConnected = false
                    } catch (e: SocketTimeoutException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                try {
                    if (ss != null)
                        ss!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }

    interface OnDataReceivedListener {
        fun onDataReceived(message: String, ip: String)
    }

    fun setOnDataReceivedListener(listener: OnDataReceivedListener) {
        this.mDataReceivedListener = listener
    }
}