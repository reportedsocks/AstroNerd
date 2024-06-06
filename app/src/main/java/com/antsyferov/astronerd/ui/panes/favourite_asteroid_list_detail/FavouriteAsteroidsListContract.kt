package com.antsyferov.astronerd.ui.panes.favourite_asteroid_list_detail

import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.LoadingState
import com.antsyferov.impl.local.AsteroidEntity

data class FavouriteAsteroidsListContract(
    val loading: LoadingState = LoadingState.Initial,
    val asteroids: List<AsteroidEntity> = emptyList()
)

