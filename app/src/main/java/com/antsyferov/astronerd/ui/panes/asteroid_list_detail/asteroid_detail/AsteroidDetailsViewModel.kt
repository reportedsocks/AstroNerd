package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroid_detail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.filter
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.AsteroidsListState
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.Filter
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.LoadingState
import com.antsyferov.impl.local.AsteroidEntity
import com.antsyferov.impl.local.AsteroidsDao
import com.antsyferov.impl.local.RemoteKey
import com.antsyferov.impl.local.RemoteKeyDao
import com.antsyferov.impl.mappers.toEntities
import com.antsyferov.impl.network.AsteroidsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AsteroidsDetailsViewModel @Inject constructor(
    private val asteroidsDao: AsteroidsDao,
    private val asteroidsApi: AsteroidsApi,
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<AsteroidDetailState> = MutableStateFlow(
        AsteroidDetailState()
    )
    val stateFlow: StateFlow<AsteroidDetailState> = _stateFlow.asStateFlow()


    fun getAsteroid(asteroidId: String) {
        viewModelScope.launch {
            try {
                val asteroid = asteroidsDao.getById(asteroidId)
                if (asteroid != null) {
                    _stateFlow.update { it.copy(loading = LoadingState.Done, asteroid = asteroid) }
                } else {
                    _stateFlow.update { it.copy(loading = LoadingState.Error) }
                }
            } catch (e: Exception) {
                _stateFlow.update { it.copy(loading = LoadingState.Error) }
            }
        }
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            _stateFlow.value.asteroid?.let {
                asteroidsDao.setFavourite(it.id, !it.isFavourite)
            }
        }
    }


}