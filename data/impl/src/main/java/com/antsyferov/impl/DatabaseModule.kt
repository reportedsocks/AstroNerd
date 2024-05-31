package com.antsyferov.impl

import android.content.Context
import androidx.room.Room
import com.antsyferov.impl.local.AppDatabase
import com.antsyferov.impl.local.AsteroidsDao
import com.antsyferov.impl.local.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDb(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "astro-db"
        )
            .build()
    }

    @Provides
    fun provideAsteroidsDao(db: AppDatabase): AsteroidsDao {
        return db.asteroidsDao()
    }

    @Provides
    fun provideRemoteKeyDao(db: AppDatabase): RemoteKeyDao {
        return db.remoteKeyDao()
    }
}