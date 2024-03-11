package com.xtenduke.nerd.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


const val PREFERENCES_TABLE_NAME = "preferences"

@Entity
data class Preferences(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "target_host") val targetHost: String?,
    @ColumnInfo(name = "target_port") val targetPort: String?
)
