package com.comtip.tip.tangran.RoomDatabase

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask

/**
 * Created by TipRayong on 2/5/2561 12:17
 * TamSangK00
 */
class FoodDataView(application: Application) : AndroidViewModel(application) {
    private val appDatabase = AppDatabase.getInDatabase(application)

    /*--------------------------------------------------------------------------
      Method : getMenuType
      Description : Get Type Food in Menu
  ---------------------------------------------------------------------------*/
    fun getMenuType(): List<String> {
        var data: List<String> = ArrayList()
        try {
            val getMenuTypeAsyncTask = GetMenuTypeAsyncTask(appDatabase)
            data = getMenuTypeAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class GetMenuTypeAsyncTask
    internal constructor(private val appDatabase: AppDatabase)
        : AsyncTask<Void, Void, List<String>>() {
        override fun doInBackground(vararg p0: Void?): List<String> {
            return appDatabase.foodDataModel().getMenuType()
        }
    }
/*--------------------------------------------------------------------------*/

    /*--------------------------------------------------------------------------
         Method : selectMenuType
         Description : Select menu by Type
     ---------------------------------------------------------------------------*/
    fun selectMenuByType(type: String): List<Food> {
        var data: List<Food> = ArrayList()
        try {
            val selectMenuByTypeAsyncTask = SelectMenuByTypeAsyncTask(appDatabase, type)
            data = selectMenuByTypeAsyncTask.execute().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return data
    }

    private class SelectMenuByTypeAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val type: String)
        : AsyncTask<Void, Void, List<Food>>() {
        override fun doInBackground(vararg p0: Void?): List<Food> {
            return appDatabase.foodDataModel().selectMenuByType(type)
        }
    }
    /*--------------------------------------------------------------------------*/

    /*--------------------------------------------------
           Method: addNewMenu
           Description: add new Menu
   --------------------------------------------------*/
    fun addNewMenu(food: Food) {
        val addNewMenuAsyncTask = AddNewMenuAsyncTask(appDatabase, food)
        addNewMenuAsyncTask.execute()
    }

    private class AddNewMenuAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val food: Food)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.foodDataModel().insertFoodMenu(food)
            return null
        }
    }
/*--------------------------------------------------------------------------*/

    /*---------------------------------------------
    Method: deleteMenu
    Description:  Delete Menu
    ---------------------------------------------*/
    fun deleteMenu(primaryKey: Int) {
        val deleteMenuAsyncTask = DeleteMenuAsyncTask(appDatabase, primaryKey)
        deleteMenuAsyncTask.execute()
    }

    private class DeleteMenuAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val primaryKey: Int)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.foodDataModel().deleteFoodMenu(primaryKey)
            return null
        }
    }
/*--------------------------------------------------------------------------*/

    /*---------------------------------------------
          Method: editMenu
          Description:  Edit Menu
       ---------------------------------------------*/
    fun editMenu(primaryKey: Int, newName: String, newType: String, newPrice: Double, newMediaUrl: String) {
        val editMenuAsyncTask = EditMenuAsyncTask(appDatabase, primaryKey, newName, newType, newPrice, newMediaUrl)
        editMenuAsyncTask.execute()
    }

    private class EditMenuAsyncTask
    internal constructor(private val appDatabase: AppDatabase,
                         private val primaryKey: Int,
                         private val newName: String,
                         private val newType: String,
                         private val newPrice: Double,
                         private val newMediaUrl: String)
        : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            appDatabase.foodDataModel().editFoodMenu(primaryKey, newName, newType, newPrice, newMediaUrl)
            return null
        }

    }
/*--------------------------------------------------------------------------*/
}

