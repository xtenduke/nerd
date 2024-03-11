package com.xtenduke.nerd.data.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PreferencesDao {
    @Query("SELECT * from $PREFERENCES_TABLE_NAME where uid = 0")
    fun getPreferences(): Preferences

    @Insert
    fun insertPreferences(preferences: Preferences)

    @Delete
    fun delete(preferences: Preferences)
}