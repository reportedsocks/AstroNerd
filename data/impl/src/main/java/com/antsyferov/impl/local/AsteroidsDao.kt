package com.antsyferov.impl.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidEntity>)

    @Query("SELECT * FROM asteroids")
    fun pagingSource(): PagingSource<Int, AsteroidEntity>

    @Query("SELECT MAX(km_estimated_diameter_max) as max, MIN(km_estimated_diameter_min) as min FROM asteroids")
    fun getMaxMinDiameter(): Flow<MaxMinDiameter>

    @Query("DELETE FROM asteroids")
    suspend fun clearAll()

    @Query("SELECT * FROM asteroids WHERE id=:id")
    suspend fun getById(id: String): AsteroidEntity?

    @Query("UPDATE asteroids SET is_favourite=:isFav WHERE id=:id")
    suspend fun setFavourite(id: String, isFav: Boolean)
}