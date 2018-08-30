package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query

/**
 * Created by TipRayong on 1/5/2561 12:43
 * TamSangK00
 */
@Dao
interface OrderingDAO {
    // แสดงรายการอาหารที่สั่ง เป็น RealTime แสดงเฉพาะฝั่ง Server
    @Query("SELECT * FROM Ordering  ORDER BY orderTime ASC")
    fun getRealTimeOrdering(): List<Ordering>

    // เแสดงรายการสั่งอาหารทั้งหมดของแต่ละโต๊ะ
    @Query("SELECT * FROM Ordering WHERE client = :client ORDER BY orderTime ASC")
    fun getAllOrdering(client: String): List<Ordering>

    //เพิ่มสั่งรายการอาหาร
    @Insert(onConflict = IGNORE)
    fun addNewOrdering(ordering: Ordering)

    // ยกเลิกสั่งรายการอาหาร 1 รายการ
    @Query("DELETE FROM Ordering WHERE primaryKey= :primaryKey")
    fun deleteOrdering(primaryKey: Long)

    //ลบสั่งรายการอาหารทั้งโต๊  ใช้ในกรณีคิดเงินเสร็จแล้ว
    @Query("DELETE FROM Ordering WHERE client= :client")
    fun deleteOrderingClient(client: String)

    //แก้ไขปรับปรุงสั่งรายการอาหาร  ปรับปรุงแก้ไขได้เฉพาะส่วนเพิ่มเติมกับจำนวน
    @Query("UPDATE Ordering SET addition = :newAddition , amount = :newAmount , orderTime = :newOrderingTime WHERE primaryKey = :primaryKey")
    fun editOrdering(primaryKey: Long, newAddition: String, newAmount: Int, newOrderingTime: String)

    @Query("SELECT DISTINCT client FROM Ordering ORDER BY client ASC")
    fun getActiveClients(): List<String>
}