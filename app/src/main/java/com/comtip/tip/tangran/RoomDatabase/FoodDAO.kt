package com.comtip.tip.tangran.RoomDatabase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query

/**
 * Created by TipRayong on 1/5/2561 12:04
 * TamSangK00
 */

@Dao
interface FoodDAO {
    //เรียกเดูประเภทเมนู  เช่น  ผัด  ต้ม  ยำ  ข้าวผัด แกง   ของหวาน  เครื่องดื่ม
    @Query("SELECT DISTINCT type FROM Food ORDER BY type ASC")
    fun getMenuType(): List<String>

    // เลือกเมนูรายการอาหารตามประเทภ  เช่น   ประเภทข้าวผัด มี ข้าวผัดหมู ข้าวผัดกระเพรา   ข้าวผัดอเมริกัน
    @Query("SELECT * FROM Food WHERE type = :type ORDER BY name ASC")
    fun selectMenuByType(type: String): List<Food>

    //เพิ่มเมนูรายการอาหารใหม่
    @Insert(onConflict = IGNORE)
    fun insertFoodMenu(food: Food)

    //ลบเมนูรายการอาหาร
    @Query("DELETE FROM Food WHERE primaryKey = :primaryKey")
    fun deleteFoodMenu(primaryKey: Int)

    //แก้ไขปรับปรุงรายการอาหาร
    @Query("UPDATE Food SET name = :newName, type = :newType, price = :newPrice, mediaUrl = :newMediaUrl WHERE primaryKey = :primaryKey")
    fun editFoodMenu(primaryKey: Int, newName: String, newType: String, newPrice: Double, newMediaUrl: String)
}