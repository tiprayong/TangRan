package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by TipRayong on 1/5/2561 12:36
 * TamSangK00
 */
@Entity
data class Ordering(
        @PrimaryKey(autoGenerate = true) var primaryKey: Long,
        var client: String,
        var food: String,
        var type: String,
        var addition: String,
        var amount: Int,
        var price: Double,
        var orderTime: String  //DD-MM-YYYY H:M:S
)
