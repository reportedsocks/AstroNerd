package com.antsyferov.astronerd.ui.panes.favourite_asteroid_list_detail

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.composables.AsteroidCard
import com.antsyferov.astronerd.ui.composables.molecule.FilterChip
import com.antsyferov.astronerd.ui.composables.template.AsteroidCardShimmer
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.LoadingState
import com.antsyferov.astronerd.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun FavouriteAsteroidsListPane(
    viewModel: FavouriteAsteroidsListViewModel = hiltViewModel(),
    onAsteroidSelected: (String) -> Unit
) {

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    LaunchedEffect(state.loading) {
        if (state.loading== LoadingState.Error) {
            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val scrollState = rememberLazyListState()
            LazyColumn(
                state = scrollState
            ) {
                items(state.asteroids, key = { it.id }) {
                    AsteroidCard(
                        diameterUnits = "km",
                        name = it.name,
                        isDangerous = it.isPotentiallyHazardousAsteroid,
                        diameterMin = it.estimatedDiameter.kilometers.min,
                        diameterMax = it.estimatedDiameter.kilometers.max,
                        orbitingBody = it.closeApproachData?.orbitingBody ?: "N/A",
                        closeApproach = it.closeApproachData?.closeApproachDate ?:"N/A",
                        onCardClick = { onAsteroidSelected.invoke(it.id) }
                    )
                }

                if (state.loading != LoadingState.Done) {
                    item {
                        AsteroidCardShimmer()
                    }
                }

            }

            if (scrollState.canScrollBackward) {
                IconButton(onClick = { coroutineScope.launch {
                    scrollState.animateScrollToItem(0, 0)
                } },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color.Black)

                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }

    }
}