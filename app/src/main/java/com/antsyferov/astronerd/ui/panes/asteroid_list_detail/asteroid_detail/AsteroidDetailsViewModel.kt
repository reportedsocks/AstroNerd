package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroid_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.LoadingState
import com.antsyferov.impl.local.AsteroidsDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AsteroidsDetailsViewModel @Inject constructor(
    private val asteroidsDao: AsteroidsDao,
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
            _stateFlow.value.asteroid?.let { asteroid ->
                asteroidsDao.setFavourite(asteroid.id, !asteroid.isFavourite)
                _stateFlow.update { it.copy(asteroid = asteroid.copy(isFavourite = !asteroid.isFavourite)) }
            }
        }
    }


}