package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by TipRayong on 1/5/2561 11:56
 * TamSangK00
 */
@Entity
data class Food(
        @PrimaryKey(autoGenerate = true) var primaryKey: Int,
        var name: String,
        var type: String,
        var price: Double,
        var mediaUrl: String
)

