package com.comtip.tip.tangran.TCP

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import java.io.*
import java.net.Socket

/**
 * Created by TipRayong on 14/5/2561 10:43
 * TangRan
 */
class TCPClient  {
    companion object {
    fun send(message: String, ip: String, port: Int) {
        TCPSend(message, ip, port).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)
    }

    fun send(message: String, ip: String, port: Int, callback: SendCallback, tag: String) {
        TCPSend(message, ip, port, callback, tag).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)
    }

    fun send(user: String, server: String, command: String, data: String, ip: String, port: Int) {
        val message = "$user:$server|$command]$data"
        TCPSend(message, ip, port).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)
    }

    fun send(user: String, server: String, command: String, data: String, ip: String, port: Int, callback: SendCallback, tag: String) {
        val message = "$user:$server|$command]$data"
        TCPSend(message, ip, port, callback, tag).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null)
    }
}

private class TCPSend : AsyncTask<Void, Void, Void> {
    private var callback: SendCallback? = null

    private var message: String? = null
    private var ip: String? = null
    private var tag: String? = null

    private var port: Int = 65534 //default port

    constructor(message: String, ip: String, port: Int) {
        this.message = message
        this.ip = ip
        this.port = port
    }

    constructor(message: String, ip: String, port: Int, callback: SendCallback, tag: String) {
        this.message = message
        this.ip = ip
        this.port = port
        this.callback = callback
        this.tag = tag
    }

    override fun doInBackground(vararg params: Void): Void? {
        try {
            val s = Socket(ip, port)
            val out = BufferedWriter(OutputStreamWriter(s.getOutputStream()))
            val outgoingMsg = message!! + System.getProperty("line.separator")
            out.write(outgoingMsg)
            out.flush()

            if (callback != null) {
                s.soTimeout = 5000
                val bufferedR = BufferedReader(InputStreamReader(s.getInputStream()))
                val inMessage = bufferedR.readLine()

                Handler(Looper.getMainLooper()).post(Runnable {
                    if (inMessage.contains("%OK%")) {
                        callback!!.onSuccess(tag)
                    } else {
                        callback!!.onFailed(tag)
                    }
                })
            }

            s.close()
        } catch (e: IOException) {
            if (callback != null) {
                Handler(Looper.getMainLooper()).post(Runnable { callback!!.onFailed(tag) })
            }
        }

        return null
    }
}

interface SendCallback {
    fun onSuccess(tag: String?)
    fun onFailed(tag: String?)
}
}