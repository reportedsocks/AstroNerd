package com.antsyferov.astronerd.ui.panes.favourite_asteroid_list_detail

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroid_detail.AsteroidDetailsPane

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavouriteAsteroidsListDetail(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            FavouriteAsteroidsListPane(
                onAsteroidSelected = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, content = it) }
            )

        },
        detailPane = {
            val asteroidId = navigator.currentDestination?.content?.toString()
            val uriHandler = LocalUriHandler.current
            AsteroidDetailsPane(
                asteroidId = asteroidId,
                onBackClick = { navigator.navigateBack() },
                onSBDClick = { uriHandler.openUri(it) }
            )
        },
        modifier = modifier
    )
}