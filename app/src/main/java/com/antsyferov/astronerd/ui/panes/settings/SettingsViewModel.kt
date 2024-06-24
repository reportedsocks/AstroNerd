package com.antsyferov.astronerd.ui.panes.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsyferov.impl.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    val flow = dataStoreManager.getIsArEnabled()

    fun setArEnabled(value: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setArEnabled(value)
        }
    }

}