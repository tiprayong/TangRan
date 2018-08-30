package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


/**
 * Created by TipRayong on 1/5/2561 13:06
 * TamSangK00
 */
@Database(entities = [(Billing::class), (Client::class), (Food::class), (Ordering::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun billingDataModel(): BillingDAO
    abstract fun clientDataModel(): ClientDAO
    abstract fun foodDataModel(): FoodDAO
    abstract fun orderingDataModel(): OrderingDAO

    companion object {
        const val DATABASE = "TangRanDB"
        private var INSTANCE: AppDatabase? = null

        fun destroyInstance() {
            INSTANCE = null
        }

        fun getInDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE)
                        .build()
            }
            return INSTANCE as AppDatabase
        }
    }
}

