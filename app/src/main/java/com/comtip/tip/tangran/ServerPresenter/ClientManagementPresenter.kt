package com.comtip.tip.tangran.ServerPresenter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.comtip.tip.tangran.Adapter.ClientAdapter
import com.comtip.tip.tangran.Control.customToast
import com.comtip.tip.tangran.R
import com.comtip.tip.tangran.RoomDatabase.Client
import com.comtip.tip.tangran.clientDataView
import kotlinx.android.synthetic.main.client_layout.*
import kotlinx.android.synthetic.main.fragment_server.view.*

/**
 * Created by TipRayong on 8/5/2561 9:55
 * TangRan
 */
fun setupClientManagementListView(activity: Activity, view: View) {
    val clients = clientDataView.getAllClients()
    if (clients.isNotEmpty()) {
        val adapter = ClientAdapter(activity, clients)
        view.mainLV.adapter = adapter
        view.mainLV.setOnItemClickListener { _, _, i, l ->

            val alertClient = AlertDialog.Builder(activity)
            alertClient.setTitle(clients[i].name)
            alertClient.setPositiveButton("Edit") { _, _ ->
                updateClientDialog(activity, view, true, clients[i])
            }

            alertClient.setNegativeButton("Delete") { _, _ ->
                deleteClientDialog(activity, view, clients[i].name)
            }

            val alertView = alertClient.create()
            alertView.show()

            val editIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(android.R.drawable.ic_menu_edit, null)
            } else {
                activity.resources.getDrawable(android.R.drawable.ic_menu_edit)
            }
            editIcon.setBounds(0, 0, editIcon.intrinsicWidth, editIcon.intrinsicHeight)
            alertView.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setCompoundDrawables(editIcon, null, null, null)

            val deleteIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.resources.getDrawable(android.R.drawable.ic_menu_delete, null)
            } else {
                activity.resources.getDrawable(android.R.drawable.ic_menu_delete)
            }
            deleteIcon.setBounds(0, 0, deleteIcon.intrinsicWidth, deleteIcon.intrinsicHeight)
            alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setCompoundDrawables(deleteIcon, null, null, null)

        }
    } else {
        view.mainLV.adapter = null
    }
}

fun deleteClientDialog(activity: Activity, view: View, key: String) {
    val alertDelete = AlertDialog.Builder(activity)
    alertDelete.setTitle("Delete $key ?")

    alertDelete.setPositiveButton("Confirm") { _, _ ->
        clientDataView.deleteClient(key)
        setupClientManagementListView(activity, view)
    }

    alertDelete.setNegativeButton("BACK") { _, _ ->
        //Cancel
    }

    val alertView = alertDelete.create()
    alertView.show()

    val confirmIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_delete, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_edit)
    }
    confirmIcon.setBounds(0, 0, confirmIcon.intrinsicWidth, confirmIcon.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_POSITIVE)
            .setCompoundDrawables(confirmIcon, null, null, null)

    val cancelIcon = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.resources.getDrawable(android.R.drawable.ic_menu_revert, null)
    } else {
        activity.resources.getDrawable(android.R.drawable.ic_menu_revert)
    }
    cancelIcon.setBounds(0, 0, cancelIcon.intrinsicWidth, cancelIcon.intrinsicHeight)
    alertView.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setCompoundDrawables(cancelIcon, null, null, null)
}


@SuppressLint("SetTextI18n")
fun updateClientDialog(activity: Activity, view: View, isEdit: Boolean, client: Client = Client("", "")) {

    val mDialog = Dialog(activity)
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog.setContentView(R.layout.client_layout)

    mDialog.setCancelable(false)

    mDialog.nameClientET.setText(client.name)
    mDialog.locationClientET.setText(client.location)


    if (isEdit) {
        mDialog.updateClientBT.text = "UPDATE"
        mDialog.nameClientET.isEnabled = false
    } else {
        mDialog.updateClientBT.text = "ADD"
    }

    mDialog.cancelClientBT.setOnClickListener {
        mDialog.dismiss()
    }

    mDialog.updateClientBT.setOnClickListener {
        if (mDialog.nameClientET.text.isNotEmpty() && mDialog.locationClientET.text.isNotEmpty()) {

            if (isEdit) {
                clientDataView.editClient(client.name, mDialog.locationClientET.text.toString())
            } else {

                if (clientDataView.isClientAvailable(mDialog.nameClientET.text.toString())) {
                    //Toast.makeText(activity, "Data Exist", Toast.LENGTH_SHORT).show()
                    customToast(activity,"Data Exist")
                } else {
                    clientDataView.addNewClient(
                            Client(mDialog.nameClientET.text.toString()
                                    , mDialog.locationClientET.text.toString()))
                    //Toast.makeText(activity, "Add Client.", Toast.LENGTH_SHORT).show()
                    customToast(activity,"Add Client")
                }
            }

            mDialog.dismiss()
            setupClientManagementListView(activity, view)

        } else {
            //Toast.makeText(activity, "Data Empty", Toast.LENGTH_SHORT).show()
            customToast(activity,"Data Empty")
        }
    }

    val windowManager = WindowManager.LayoutParams()
    windowManager.copyFrom(mDialog.window.attributes)
    windowManager.width = WindowManager.LayoutParams.MATCH_PARENT
    windowManager.height = WindowManager.LayoutParams.WRAP_CONTENT
    mDialog.window.attributes = windowManager
    mDialog.show()
}


