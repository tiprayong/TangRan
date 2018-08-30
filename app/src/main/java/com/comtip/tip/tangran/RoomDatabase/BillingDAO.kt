package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query

/**
 * Created by TipRayong on 1/5/2561 12:54
 * TamSangK00
 */
@Dao
interface BillingDAO {
    // เรียกดู Detail ในแต่ละช่วง โดยดูได้เป็นรายปี   ( เช่น 2017)   รายเดือน  ( เช่น 201704)     รายวัน เช่น ( เช่น 20170428)
    @Query("SELECT detail FROM Billing WHERE time LIKE :time||'%' ORDER BY time ASC")
    fun getDetailByTime(time: String): List<String>

    //บันทึก Billing
    @Insert(onConflict = IGNORE)
    fun addBilling(billing: Billing)

    // ลบ Billing กำหนดให้ลบได้ทีละบิลเท่านั้น เพื่อป้องกันเจตนาทุจริต
    @Query("DELETE FROM Billing WHERE time= :time")
    fun deleteBilling(time: String)
}