package com.comtip.tip.tangran.RoomDatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask

/**
 * Created by TipRayong on 2/5/2561 13:14
 * TamSangK00
 */
class OrderingDataView(application: Application) : AndroidViewModel(application) {
    private val appDatabase = AppDatabase.getInDatabase(application)

    /*--------------------------------------------------------------------------
         Method : getAllOrdering
         Description : get All Orders
     ---------------------------------------------------------------------------*/
    fun getAllOrdering(client: String): List<Ordering> {
        var data: List<Ordering> = ArrayList()
        try {
            val getAllOrderingAsyncTask = GetAllOrderingAsyncTask(appDatabase, client)
            data = getAllOrderingAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetAllOrderingAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val client: String)
        : AsyncTask<Void, Void, List<Ordering>>() {
        override fun doInBackground(vararg p0: Void?): List<Ordering> {
            return appDatabase.orderingDataModel().getAllOrdering(client)
        }
    }

    /*--------------------------------------------------------------------------*/

    /*--------------------------------------------------
               Method: addNewOrdering
               Description: add new Order
       --------------------------------------------------*/
    fun addNewOrdering(ordering: Ordering) {
        val addNewOrderingAsyncTask = AddNewOrderingAsyncTask(appDatabase, ordering)
        addNewOrderingAsyncTask.execute()
    }

    private class AddNewOrderingAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val ordering: Ordering)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.orderingDataModel().addNewOrdering(ordering)
            return null
        }
    }
    /*--------------------------------------------------------------------------*/

    /*---------------------------------------------
    Method: deleteOrdering
    Description:  Delete 1 Ordering
    ---------------------------------------------*/
    fun deleteOrdering(primaryKey: Long) {
        val deleteOrderingAsyncTask = DeleteOrderingAsyncTask(appDatabase, primaryKey)
        deleteOrderingAsyncTask.execute()
    }

    private class DeleteOrderingAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val primaryKey: Long)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.orderingDataModel().deleteOrdering(primaryKey)
            return null
        }
    }
/*--------------------------------------------------------------------------*/

    /*---------------------------------------------
    Method: deleteOrderingClient
    Description:  Delete All Ordering ('s Client)
    ---------------------------------------------*/
    fun deleteOrderingClient(client: String) {
        val deleteOrderingClientAsyncTask = DeleteOrderingClientAsyncTask(appDatabase, client)
        deleteOrderingClientAsyncTask.execute()
    }

    private class DeleteOrderingClientAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val client: String)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.orderingDataModel().deleteOrderingClient(client)
            return null
        }
    }
    /*--------------------------------------------------------------------------*/

    /*---------------------------------------------
          Method: editMenu
          Description:  Edit Menu
       ---------------------------------------------*/
    fun editOrdering(primaryKey: Long, newAddition: String, newAmount: Int, newOrderingTime: String) {
        val editOrderingAsyncTask = EditOrderingAsyncTask(appDatabase, primaryKey, newAddition, newAmount, newOrderingTime)
        editOrderingAsyncTask.execute()
    }

    private class EditOrderingAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val primaryKey: Long,
                         private val newAddition: String,
                         private val newAmount: Int,
                         private val newOrderingTime: String)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.orderingDataModel().editOrdering(primaryKey, newAddition, newAmount, newOrderingTime)
            return null
        }

    }
/*--------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------
        Method : getRealTimeOrdering
        Description : get RealTime Orders
    ---------------------------------------------------------------------------*/
    fun getRealTimeOrdering(): List<Ordering> {
        var data: List<Ordering> = ArrayList()
        try {

            val getRealTimeOrderingAsyncTask = GetRealTimeOrderingAsyncTask(appDatabase)
            data = getRealTimeOrderingAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetRealTimeOrderingAsyncTask
    internal constructor(private val appDatabase: AppDatabase)
        : AsyncTask<Void, Void, List<Ordering>>() {
        override fun doInBackground(vararg p0: Void?): List<Ordering> {
            return appDatabase.orderingDataModel().getRealTimeOrdering()
        }
    }

    /*--------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------
     Method : getMenuType
     Description : Get Type Food in Menu
 ---------------------------------------------------------------------------*/
    fun getActiveClients(): List<String> {
        var data: List<String> = ArrayList()
        try {
            val getActiveClientsAsyncTask = GetActiveClientsAsyncTask(appDatabase)
            data = getActiveClientsAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetActiveClientsAsyncTask
    internal constructor(private val appDatabase: AppDatabase)
        : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg p0: Void?): List<String> {
            return appDatabase.orderingDataModel().getActiveClients()
        }
    }
/*--------------------------------------------------------------------------*/
}


