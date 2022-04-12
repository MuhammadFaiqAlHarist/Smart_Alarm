package com.faiq.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.faiq.data.Alarm

@Dao
interface AlarmDao {

    @Insert
    fun addAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm")
    fun getAlarm() : LiveData<List<Alarm>>

    @Delete
    fun deleteAlarm(alarm: Alarm)
}
