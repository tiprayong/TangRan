package com.comtip.tip.tangran.WebPresenter

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebSettings
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.restaurant
import kotlinx.android.synthetic.main.webview_layout.*


/**
 * Created by TipRayong on 6/6/2561 10:47
 * TangRan
 */
class ActivityWebView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_layout)
        supportActionBar!!.title = restaurant

        viewBT.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val bundle = intent.extras
        val url = bundle.getString("mediaUrl", "")
        val name = bundle.getString("name", "")
        if (url.isNotEmpty()) {
            setupWebView(name, url)
        } else {
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(name: String, url: String) {
        viewBT.text = name

        //  viewWV.settings.javaScriptEnabled = true
        //  viewWV.settings.allowFileAccess = true
        viewWV.settings.setSupportZoom(true)
        viewWV.settings.builtInZoomControls = true
        viewWV.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        viewWV.loadUrl(url)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}