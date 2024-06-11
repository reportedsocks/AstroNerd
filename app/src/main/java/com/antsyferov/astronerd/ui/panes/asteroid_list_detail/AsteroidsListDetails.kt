package com.antsyferov.astronerd.ui.panes.asteroid_list_detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroid_detail.AsteroidDetailsPane
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.AsteroidsListPane

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AsteroidsListDetail(
    modifier: Modifier = Modifier,
    onVisualizationClick: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AsteroidsListPane(
                onAsteroidSelected = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, content = it) }
            )

        },
        detailPane = {
            val asteroidId = navigator.currentDestination?.content?.toString()
            val uriHandler = LocalUriHandler.current
            AsteroidDetailsPane(
                asteroidId = asteroidId,
                onBackClick = { navigator.navigateBack() },
                onSBDClick = { uriHandler.openUri(it) },
                onVisualizationClick = onVisualizationClick
            )
        },
        modifier = modifier
    )
}