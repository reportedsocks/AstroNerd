package com.antsyferov.astronerd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.antsyferov.astronerd.ui.navigation.AppNavHost
import com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list.AsteroidsListPane
import com.antsyferov.astronerd.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {

                val navItems = listOf("Home" to Icons.Default.Home, "Favourites" to ImageVector.vectorResource(R.drawable.ic_bookmark_fill))
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationSuiteScaffold(
                    navigationSuiteItems = {
                        navItems.fastForEachIndexed { index, item ->
                            item(
                                icon = { Icon(imageVector = item.second, contentDescription = item.first) },
                                selected = currentRoute.equals(item.first, true),
                                onClick = {
                                    navController.navigate(item.first.lowercase())
                                }
                            )
                        }

                    },
                    navigationSuiteColors = NavigationSuiteDefaults.colors(
                        navigationBarContainerColor = AppTheme.colors.secondaryGray100,
                        navigationRailContainerColor = AppTheme.colors.secondaryGray100,
                        navigationBarContentColor = Color.Black
                    ),
                    containerColor = Color.Black
                ){
                    AppNavHost(navHostController = navController)
                }
            }
        }
    }
}


