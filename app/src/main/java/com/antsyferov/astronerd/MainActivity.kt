package com.antsyferov.astronerd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.antsyferov.astronerd.ui.panes.AsteroidsListPane
import com.antsyferov.astronerd.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Scaffold { innerPadding ->
                    AsteroidsListDetail(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AsteroidsListDetail(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AsteroidsListPane {
               navigator.navigateTo(ThreePaneScaffoldRole.Secondary, content = it)
            }
        },
        detailPane = {
            val asteroidId = navigator.currentDestination?.content?.toString()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()) {
                Column {
                    Text(
                        text = "Detail screen"
                    )
                    if (asteroidId != null) {
                        Text(
                            text = "Selected asteroid id: $asteroidId"
                        )
                    } else {
                        Text(
                            text = "Select an asteroid"
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}

