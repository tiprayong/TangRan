package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by TipRayong on 1/5/2561 12:19
 * TamSangK00
 */
@Entity
data class Client(
        @PrimaryKey val name: String,
        var location: String)