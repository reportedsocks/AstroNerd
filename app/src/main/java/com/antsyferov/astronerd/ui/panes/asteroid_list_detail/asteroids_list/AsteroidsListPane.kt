package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list

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
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.antsyferov.astronerd.ui.composables.molecule.PrimaryFAB
import com.antsyferov.astronerd.ui.composables.template.AsteroidCardShimmer
import com.antsyferov.astronerd.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AsteroidsListPane(
    viewModel: AsteroidsListViewModel = hiltViewModel(),
    onAsteroidSelected: (String) -> Unit,
    onVisualizationClick: () -> Unit,
) {

    val asteroids = viewModel.asteroidsFlow.collectAsLazyPagingItems(Dispatchers.IO)

    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    LaunchedEffect(state.loading) {
        if (state.loading== LoadingState.Error) {
            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
        }
    }

    if (showBottomSheet) {
        FilterBottomSheet(
            sheetState = sheetState,
            filters = state.filters,
            range = state.range,
            onDismiss = { showBottomSheet = false },
            onAddFilter = { viewModel.addFilter(it) }
        )
    }

    Scaffold(
        floatingActionButton = {
            PrimaryFAB(
                iconRes = R.drawable.ic_ar,
                onFabClick = onVisualizationClick,
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(padding),
            color = AppTheme.colors.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val scrollState = rememberLazyListState()

                LaunchedEffect(asteroids.loadState.append.endOfPaginationReached) {
                    if (asteroids.loadState.append.endOfPaginationReached && state.loading == LoadingState.Done) {
                        viewModel.loadNextPage()
                    }
                }
                Column {

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Filters:",
                        style = AppTheme.typography.semibold16,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .animateContentSize()
                    ) {
                        for (filter in state.filters) {
                            when(filter) {
                                is Filter.Dangerous -> {
                                    FilterChip(title = "Is dangerous:", value = filter.isDangerous.toString()) {
                                        viewModel.removeFilter(filter)
                                    }
                                }
                                is Filter.Name -> {
                                    FilterChip(title = "Name:", value = filter.name) {
                                        viewModel.removeFilter(filter)
                                    }
                                }
                                is Filter.Orbiting -> {
                                    FilterChip(title = "Orbiting body:", value = filter.body) {
                                        viewModel.removeFilter(filter)
                                    }
                                }
                                is Filter.Date -> {
                                    FilterChip(title = "Close approach:", value = filter.date) {
                                        viewModel.removeFilter(filter)
                                    }
                                }
                                is Filter.Diameter -> {
                                    FilterChip(
                                        title = "Diameter:",
                                        value = stringResource(id = R.string.diameter_range, filter.range.start, filter.range.endInclusive)
                                    ) {
                                        viewModel.removeFilter(filter)
                                    }
                                }
                            }
                        }

                        FilterChip(isAdd = true) {
                            showBottomSheet = true
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        state = scrollState
                    ) {

                        items(asteroids.itemCount, key = { i -> asteroids[i]?.id ?: UUID.randomUUID()}) { index ->
                            asteroids[index]?.let {
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
                        }

                        if (!scrollState.canScrollForward || state.loading != LoadingState.Done) {
                            item {
                                AsteroidCardShimmer()
                            }
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


}