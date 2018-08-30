package com.comtip.tip.tangran.RoomDatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask

/**
 * Created by TipRayong on 1/5/2561 14:13
 * TamSangK00
 */
class ClientDataView(application: Application) : AndroidViewModel(application) {
    private val appDatabase = AppDatabase.getInDatabase(application)

    /*--------------------------------------------------------------------------
        Method : getAllClients
        Description : Get All Clients
    ---------------------------------------------------------------------------*/
    fun getAllClients(): List<Client> {
        var data: List<Client> = ArrayList()
        try {
            val getAllClientAsyncTask = GetAllClientsAsyncTask(appDatabase)
            data = getAllClientAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetAllClientsAsyncTask
    internal constructor(private val appDatabase: AppDatabase)
        : AsyncTask<Void, Void, List<Client>>() {
        override fun doInBackground(vararg p0: Void?): List<Client> {
            return appDatabase.clientDataModel().getAllClients()
        }
    }
/*--------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------
                Method : isClientAvailable
                Description : Check Exist Client
    ---------------------------------------------------------------------------*/
    fun isClientAvailable(name: String): Boolean {
        var data: List<String> = ArrayList()
        try {
            val isClientAvailableAsyncTask = IsClientAvailableAsyncTask(appDatabase, name)
            data = isClientAvailableAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data.size > 0
    }

    private class IsClientAvailableAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val name: String)
        : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg p0: Void?): List<String> {
            return appDatabase.clientDataModel().isClientAvailable(name)
        }
    }
/*--------------------------------------------------------------------------*/


    /*--------------------------------------------------
            Method: addNewClient
            Description: add new Client
    --------------------------------------------------*/
    fun addNewClient(client: Client) {
        val addNewClientsAsyncTask = AddNewClientAsyncTask(appDatabase, client)
        addNewClientsAsyncTask.execute()
    }

    private class AddNewClientAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val client: Client)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.clientDataModel().addNewClient(client)
            return null
        }
    }
/*--------------------------------------------------------------------------*/

    /*---------------------------------------------
      Method: deleteClient
      Description:  Delete Client
   ---------------------------------------------*/
    fun deleteClient(name: String) {
        val deleteClientAsyncTask = DeleteClientAsyncTask(appDatabase, name)
        deleteClientAsyncTask.execute()
    }

    private class DeleteClientAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val name: String)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.clientDataModel().deleteClient(name)
            return null
        }
    }
/*--------------------------------------------------------------------------*/

    /*---------------------------------------------
      Method: editClient
      Description:  Edit Client
   ---------------------------------------------*/
    fun editClient(name: String, location: String) {
        val editClientAsyncTask = EditClientAsyncTask(appDatabase, name, location)
        editClientAsyncTask.execute()
    }

    private class EditClientAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val name: String,
                         private val location: String)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.clientDataModel().editClient(name, location)
            return null
        }

    }
/*--------------------------------------------------------------------------*/
}

