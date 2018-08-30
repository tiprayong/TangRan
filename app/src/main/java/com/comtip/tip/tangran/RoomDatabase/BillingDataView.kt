package com.comtip.tip.tangran.RoomDatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask

/**
 * Created by TipRayong on 1/5/2561 13:10
 * TamSangK00
 */
class BillingDataView(application: Application) : AndroidViewModel(application) {
    private val appDatabase = AppDatabase.getInDatabase(application)

    /*--------------------------------------------------------------------------
            Method: getDetailByTime
            Description: "SELECT detail FROM Billing WHERE time LIKE :time||'%' ORDER BY time ASC"
    ---------------------------------------------------------------------------*/
    fun getDetailByTime(time: String): List<String> {
        var data: List<String> = ArrayList()
        try {
            val getDetailByTimeAsyncTask = GetDetailByTimeAsyncTask(appDatabase, time)
            data = getDetailByTimeAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetDetailByTimeAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val time: String)
        : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg p0: Void?): List<String> {
            return appDatabase.billingDataModel().getDetailByTime(time)
        }
    }
/*--------------------------------------------------------------------------*/

    /*--------------------------------------------------
        Method:  addBilling
        Description:  Insert Billing to Table.
    --------------------------------------------------*/
    fun addBilling(billing: Billing) {
        val addBillingTask = AddBillingAsyncTask(appDatabase, billing)
        addBillingTask.execute()
    }

    private class AddBillingAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val billing: Billing)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.billingDataModel().addBilling(billing)
            return null
        }
    }
/*--------------------------------------------------------------------------*/

    /*---------------------------------------------
        Method: deleteBilling
        Description:  Delete billing from Table.
     ---------------------------------------------*/
    fun deleteBilling(time: String) {
        val deleteBillingTask = DeleteBillingAsyncTask(appDatabase, time)
        deleteBillingTask.execute()
    }

    private class DeleteBillingAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val time: String)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.billingDataModel().deleteBilling(time)
            return null
        }
    }
/*--------------------------------------------------------------------------*/
}
