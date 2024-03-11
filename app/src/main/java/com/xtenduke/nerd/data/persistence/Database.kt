package com.xtenduke.nerd.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Preferences::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun preferencesDao(): PreferencesDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): Any = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "db",
            ).also { instance = it }
        }
    }
}