package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroid_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.composables.molecule.DetailsMainContentHeader
import com.antsyferov.astronerd.ui.composables.molecule.PageIndicator
import com.antsyferov.astronerd.ui.composables.molecule.PrimaryFAB
import com.antsyferov.astronerd.ui.composables.molecule.SecondaryTopBar
import com.antsyferov.astronerd.ui.composables.template.DetailsTable
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.LoadingState
import com.antsyferov.astronerd.ui.theme.AppTheme
import com.antsyferov.impl.local.AsteroidEntity
import kotlinx.coroutines.launch



@Composable
fun AsteroidDetailsPane(
    asteroidId: String?,
    viewModel: AsteroidsDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSBDClick: (String) -> Unit,
    onVisualizationClick: () -> Unit
) {
    if (asteroidId != null) {
        LaunchedEffect(asteroidId) {
            viewModel.getAsteroid(asteroidId)
        }
        val state by viewModel.stateFlow.collectAsStateWithLifecycle()
        if (state.loading is LoadingState.Done) {
            state.asteroid?.let {
                DetailsScreenComposable(
                    data = it,
                    onBackClick = onBackClick,
                    onSaveClick = { viewModel.toggleFavourite() },
                    isSavedAsteroid = it.isFavourite,
                    onVisualizationClick = onVisualizationClick,
                    compareFabVisibility = false,
                    onSbdClick = onSBDClick,
                    isDistanceDanger = false
                )
            }
        }
    } else {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
        ) {
            Text(
                text = "Select an asteroid to inspect details"
            )
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailsScreenComposable(
    data: AsteroidEntity,
    onBackClick: () -> Unit,
    onSaveClick: (value: Boolean) -> Unit,
    isSavedAsteroid: Boolean,
    onVisualizationClick: () -> Unit,
    compareFabVisibility: Boolean,
    onSbdClick: (url: String) -> Unit,
    isDistanceDanger: Boolean
) {

    val pagerState = rememberPagerState(
        pageCount = {
            2
        },
        initialPage = if (isDistanceDanger) 1 else 0
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        /*when (data) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background), contentAlignment = Alignment.Center
                ) {
                    InsertLoader(text = "Loading asteroid details")
                }
            }

            is UiState.Error -> InsertError()
            is UiState.Success -> {*/
                DetailsMainContent(
                    data = data,
                    pagerState = pagerState,
                    //userPrefs = data.userPrefs,
                    onBackClick = onBackClick,
                    onSaveClick = onSaveClick,
                    isSavedAsteroid = isSavedAsteroid,
                    onVisualizationClick = onVisualizationClick,
                    compareFabVisibility = compareFabVisibility,
                    onSbdClick = onSbdClick,
                    isDistanceDanger = isDistanceDanger
                )
            }
        //}
   // }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsMainContent(
    data: AsteroidEntity,
    pagerState: PagerState,
    //userPrefs: UserPreferencesModel?,
    onBackClick: () -> Unit,
    onSaveClick: (value: Boolean) -> Unit,
    isSavedAsteroid: Boolean,
    onVisualizationClick: () -> Unit,
    compareFabVisibility: Boolean,
    onSbdClick: (url: String) -> Unit,
    isDistanceDanger: Boolean
) {

    val comparePagerState =
        rememberPagerState(
            pageCount = { 2 },
            initialPage = if (isDistanceDanger) 1 else 0
        )
    var selectedIndex by remember { mutableIntStateOf(if (isDistanceDanger) 1 else 0) }

    val coroutineScope = rememberCoroutineScope()

    val closeApproach = data.closeApproachData//[0] // This list contains only one item with most closest date
    val astronomicalDistance = closeApproach!!.missDistance.astronomical

    val convertedDiameterToKm = data.estimatedDiameter.kilometers.max.toFloat()/*when (userPrefs?.diameterUnits) {
        DiameterUnit.KILOMETER -> data.estimatedDiameter.kilometers.estimatedDiameterMax.toFloat()
        DiameterUnit.METER -> ConvertDiameterToKm.metersToKilometers(data.estimatedDiameter.meters.estimatedDiameterMax.toFloat())
        DiameterUnit.MILE -> ConvertDiameterToKm.milesToKilometers(data.estimatedDiameter.miles.estimatedDiameterMax.toFloat())
        DiameterUnit.FEET -> ConvertDiameterToKm.feetToKilometers(data.estimatedDiameter.feet.estimatedDiameterMax.toFloat())
        else -> 0f
    }*/

    Scaffold(
        containerColor = AppTheme.colors.background,
        topBar = {
            SecondaryTopBar(
                title = "Asteroid Details",
                onBackClick = onBackClick,
                onFavoriteClick = onSaveClick,
                isFavorite = isSavedAsteroid
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PrimaryFAB(
                    iconRes = R.drawable.ic_ar,
                    onFabClick = onVisualizationClick,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .background(AppTheme.colors.background)
                .verticalScroll(rememberScrollState())
        ) {
            DetailsMainContentHeader(
                name = data.name,
                id = data.id,
                sbdAction = {
                    onSbdClick(data.nasaJplUrl)
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = tween(200, easing = FastOutSlowInEasing)
                    )
                    .padding(top = 32.dp),
                verticalAlignment = Alignment.Top,

                ) {
                when (it) {
                    0 -> DetailsTable(
                        modifier = Modifier,
                        data = data,
                    )

                    1 -> DetailsCompareScreen(
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                        objectName = data.name,
                        objectSize = convertedDiameterToKm,
                        astronomicalDistance = astronomicalDistance.toFloat(),
                        comparePagerState = comparePagerState,
                        switchIndexPosition = selectedIndex,
                        onSwitchIndexPosition = {
                            selectedIndex = it
                            coroutineScope.launch {
                                comparePagerState.animateScrollToPage(it)
                            }
                        }
                    )
                }
            }

            PageIndicator(
                pagerState = pagerState,
                pageCount = pagerState.pageCount,
                modifier = androidx.compose.ui.Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}