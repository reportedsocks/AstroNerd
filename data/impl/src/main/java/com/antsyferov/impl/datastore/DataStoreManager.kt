package com.antsyferov.impl.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) {

    private val datastore = applicationContext.dataStore

    private val isAREnabledKey = booleanPreferencesKey("AR")

    fun getIsArEnabled(): Flow<Boolean> {
        return datastore.data.map {
            it[isAREnabledKey] ?: false
        }
    }

    suspend fun setArEnabled(value: Boolean) {
        datastore.edit {
            it[isAREnabledKey] = value
        }
    }

}