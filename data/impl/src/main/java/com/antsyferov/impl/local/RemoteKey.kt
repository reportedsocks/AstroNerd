package com.antsyferov.impl.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo("next_start")
    val nextStart: String,
    @ColumnInfo("next_end")
    val nextEnd: String,
    @ColumnInfo("timestamp")
    val timestamp: Long
)

