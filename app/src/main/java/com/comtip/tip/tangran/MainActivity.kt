package com.comtip.tip.tangran

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.comtip.tip.tangran.Control.*
import com.comtip.tip.tangran.Fragment.ClientFragment
import com.comtip.tip.tangran.Fragment.ServerFragment
import com.comtip.tip.tangran.RoomDatabase.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadShared(this)

        //todo  ปิดเปลี่ยนภาษาชั่วคราว  ค่อยไปจัดการ values ทีหลัง
        //  setLanguage(this, language)

        setContentView(R.layout.activity_main)

        //Header Action Bar
        supportActionBar!!.title = restaurant

        //----Room---- Management Database (Server Side Only)
        billingDataView = ViewModelProviders.of(this).get(BillingDataView::class.java)
        clientDataView = ViewModelProviders.of(this).get(ClientDataView::class.java)
        foodDataView = ViewModelProviders.of(this).get(FoodDataView::class.java)
        orderingDataView = ViewModelProviders.of(this).get(OrderingDataView::class.java)
        //------------

        if (savedInstanceState == null) {
            if (isServer) {
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.containerLayout, ServerFragment.newInstance())
                        .commit()
            } else {
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.containerLayout, ClientFragment.newInstance())
                        .commit()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (serverName.isEmpty()) {
            usePermission = true
        }

        if (usePermission) {

            when (item!!.itemId) {
                R.id.server_menu -> {
                    usePermission = false
                    ipConfigDialog(this)
                    return true
                }

                R.id.status_menu -> {
                    usePermission = false
                    val alertStatus = AlertDialog.Builder(this)
                    alertStatus.setTitle("Change Status")
                    alertStatus.setPositiveButton("Server") { _, _ ->
                        isServer = true
                        finish()
                    }

                    alertStatus.setNegativeButton("Client") { _, _ ->
                        isServer = false
                        finish()
                    }
                    alertStatus.create().show()
                    return true
                }

                R.id.identity_menu -> {
                    usePermission = false
                    identityConfigDialog(this)
                    return true
                }

                R.id.language_menu -> {
                    usePermission = false
                    val alertStatus = AlertDialog.Builder(this)
                    alertStatus.setTitle("Change Language")
                    alertStatus.setPositiveButton("ENGLISH") { _, _ ->
                        language = ""
                        customToast(this, "Pleas Restart Application.")
                    }

                    alertStatus.setNegativeButton("ไทย") { _, _ ->
                        language = "th"
                        customToast(this, "กรุณาปิดและเปิดแอพใหม่")
                    }
                    alertStatus.create().show()
                    return true
                }

                R.id.size_menu -> {
                    usePermission = false
                    val alertStatus = AlertDialog.Builder(this)
                    alertStatus.setTitle("Text Size")
                    alertStatus.setPositiveButton("Small") { _, _ ->
                        textSize = 14f
                        finish()
                    }

                    alertStatus.setNegativeButton("Medium") { _, _ ->
                        textSize = 18f
                        finish()
                    }

                    alertStatus.setNeutralButton("Large") { _, _ ->
                        textSize = 22f
                        finish()
                    }

                    alertStatus.create().show()
                    return true
                }

                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
        } else {

            passwordDialog(this)

            return super.onOptionsItemSelected(item)
        }

    }

    override fun onPause() {
        super.onPause()
        saveShared(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }


}
