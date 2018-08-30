package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by TipRayong on 1/5/2561 12:53
 * TamSangK00
 */
@Entity
data class Billing(
        @PrimaryKey val time: String, //yyyyMMdd-HH:mm:ss:SSS
        var Client: String,
        var payment: Double,
        var detail: String // รายละเอียดรายการอาหารทั้งทมดที่สั่ง  เก็บในรูปแบบ JSON  String
)
