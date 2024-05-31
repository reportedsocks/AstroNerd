package com.antsyferov.impl.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("SELECT * FROM remote_keys ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastKey(): RemoteKey?

    @Query("SELECT * FROM remote_keys ORDER BY timestamp ASC LIMIT 1")
    suspend fun getFirstKey(): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()

}