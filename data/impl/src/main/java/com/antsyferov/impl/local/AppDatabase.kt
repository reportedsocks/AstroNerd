package com.antsyferov.impl.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        AsteroidEntity::class,
        RemoteKey::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun asteroidsDao(): AsteroidsDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}