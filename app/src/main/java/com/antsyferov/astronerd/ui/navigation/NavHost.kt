package com.antsyferov.astronerd.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.AsteroidsListDetail
import com.antsyferov.astronerd.ui.panes.favourite_asteroid_list_detail.FavouriteAsteroidsListDetail
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun AppNavHost(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = "home") {
        composable("home") {
            AsteroidsListDetail()
        }
        composable("favourites") {
            FavouriteAsteroidsListDetail()
        }
    }
}