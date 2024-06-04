package com.antsyferov.astronerd.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.AsteroidsListDetail

@Composable
fun AppNavHost(
    navHostController: NavHostController
) {
    NavHost(navController = navHostController, startDestination = "home") {
        composable("home") {
            AsteroidsListDetail()
        }
        composable("favourites") {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Favourites")
            }
        }
    }
}