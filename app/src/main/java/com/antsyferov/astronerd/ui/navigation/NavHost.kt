package com.antsyferov.astronerd.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.AsteroidsListDetail
import com.antsyferov.astronerd.ui.panes.asteroid_visualization.AsteroidVisualization
import com.antsyferov.astronerd.ui.panes.favourite_asteroid_list_detail.FavouriteAsteroidsListDetail
import com.antsyferov.astronerd.ui.panes.settings.Settings
import com.antsyferov.astronerd.ui.panes.visualization.Visualization
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun AppNavHost(
    navHostController: NavHostController
) {
    fun onNavToVisualization() {
        navHostController.navigate("visualization")
    }

    fun onNavToAsteroidVisualization(id: String) {
        navHostController.navigate("asteroid_visualization/$id")
    }

    NavHost(navController = navHostController, startDestination = "home") {

        composable("home") {
            AsteroidsListDetail(
                onVisualizationClick = ::onNavToVisualization,
                onAsteroidVisualizationClick = ::onNavToAsteroidVisualization
            )
        }
        composable("favourites") {
            FavouriteAsteroidsListDetail(
                onVisualizationClick = ::onNavToVisualization,
                onAsteroidVisualizationClick = ::onNavToAsteroidVisualization
            )
        }
        composable("visualization") {
            Visualization()
        }
        composable("settings") {
            Settings()
        }
        composable(
            "asteroid_visualization/{asteroid_id}",
            arguments = listOf(navArgument("asteroid_id") { type = NavType.StringType})
        ) {
            val asteroidId = it.arguments?.getString("asteroid_id") ?: ""
            AsteroidVisualization(asteroidId = asteroidId)
        }
    }
}