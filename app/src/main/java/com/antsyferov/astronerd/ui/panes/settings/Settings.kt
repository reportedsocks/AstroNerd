package com.antsyferov.astronerd.ui.panes.settings

import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antsyferov.astronerd.ui.theme.AppTheme
import com.google.ar.core.ArCoreApk
import io.github.sceneview.ar.ARCore

@Composable
fun Settings(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    Scaffold { padding ->

        val context = LocalContext.current
        var isARSupported by remember { mutableStateOf(false) }
        val isArEnabled by viewModel.flow.collectAsStateWithLifecycle(initialValue = false)
        val areRealDistances by viewModel.realDistancesFlow.collectAsStateWithLifecycle(initialValue = false)

        LaunchedEffect(Unit) {
            ArCoreApk.getInstance().checkAvailabilityAsync(context) { availability ->
                isARSupported = availability.isSupported
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                text = "AR support",
                style = AppTheme.typography.bold18
            )
            Spacer(modifier = Modifier.height(4.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppTheme.colors.border)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isARSupported) "Supported on your device" else "Not supported on your device",
                style = AppTheme.typography.medium16,
                color = if (isARSupported) Color.Green else Color.Red
            )

            if (isARSupported) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Enabled:",
                        style = AppTheme.typography.bold16
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isArEnabled,
                        onCheckedChange = {
                            viewModel.setArEnabled(it)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Enable real scaling of distances:",
                    style = AppTheme.typography.bold16
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = areRealDistances,
                    onCheckedChange = {
                        viewModel.setAreRealDistancesEnabled(it)
                    }
                )
            }
        }
    }
}