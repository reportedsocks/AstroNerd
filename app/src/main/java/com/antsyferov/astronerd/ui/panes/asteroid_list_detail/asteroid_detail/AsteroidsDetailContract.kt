package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroid_detail

import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.LoadingState
import com.antsyferov.impl.local.AsteroidEntity

data class AsteroidDetailState(
    val loading: LoadingState = LoadingState.Initial,
    val asteroid: AsteroidEntity? = null
)


