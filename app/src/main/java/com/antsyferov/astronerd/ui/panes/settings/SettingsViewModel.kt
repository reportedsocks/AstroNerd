package com.antsyferov.astronerd.ui.panes.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsyferov.impl.datastore.DataStoreManager
import com.antsyferov.impl.local.AsteroidEntity
import com.antsyferov.impl.local.AsteroidsDao
import com.antsyferov.impl.network.AsteroidsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val asteroidsDao: AsteroidsDao,
    private val asteroidsApi: AsteroidsApi
): ViewModel() {

    val flow = dataStoreManager.getIsArEnabled()
    val realDistancesFlow = dataStoreManager.getAreRealDistancesEnabled()
    private val _asteroidFlow: MutableStateFlow<AsteroidEntity?> = MutableStateFlow(null)
    val asteroidFlow:StateFlow<AsteroidEntity?> = _asteroidFlow.asStateFlow()

    fun setArEnabled(value: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setArEnabled(value)
        }
    }

    fun getAsteroid(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            asteroidsDao.getById(id)?.let {
                _asteroidFlow.value = it
            }
            try {
                val orbitData = asteroidsApi.getAsteroidDetails(id)

                _asteroidFlow.update {
                    it?.orbitData = orbitData.orbitalData
                    it
                }
            } catch (e: Exception) {
                Log.e("MyLogs", e.message ?:" exc")
            }

        }
    }

    fun setAreRealDistancesEnabled(value: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setAreRealDistancesEnabled(value)
        }
    }
}