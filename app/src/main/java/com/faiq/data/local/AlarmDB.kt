package com.faiq.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.faiq.data.Alarm


@Database(entities = [Alarm::class], version = 2)
abstract class AlarmDB : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        var instance: AlarmDB? = null

        @JvmStatic
        fun getDatabase(context: Context): AlarmDB {
            if (instance == null) {
                synchronized(AlarmDB::class.java) {
                    instance = Room.databaseBuilder(
                        context, AlarmDB::class.java, "smart_alarm_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance as AlarmDB
        }
    }
}
