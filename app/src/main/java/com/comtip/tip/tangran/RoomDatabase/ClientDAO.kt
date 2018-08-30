package com.comtip.tip.tangran.RoomDatabase


import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.IGNORE
import android.arch.persistence.room.Query

/**
 * Created by TipRayong on 1/5/2561 12:25
 * TamSangK00
 */
@Dao
interface ClientDAO {
    // เแสดงโต๊ะทั้งหมด
    @Query("SELECT * FROM Client ORDER BY name ASC")
    fun getAllClients(): List<Client>

    //ตรวจสอบว่ามีชื่อโต๊ะนี้อยู่ในระบบหรือไม่
    @Query("SELECT name FROM Client WHERE name = :name")
    fun isClientAvailable(name: String): List<String>

    //เพิ่มเโต๊ะ
    @Insert(onConflict = IGNORE)
    fun addNewClient(client: Client)

    //ลบโต๊ะ
    @Query("DELETE FROM Client WHERE name= :name")
    fun deleteClient(name: String)

    //แก้ไขปรับโต๊ะ แก้ได้เฉพาะ Location
    @Query("UPDATE Client SET location = :location WHERE name = :name")
    fun editClient(name: String, location: String)
}