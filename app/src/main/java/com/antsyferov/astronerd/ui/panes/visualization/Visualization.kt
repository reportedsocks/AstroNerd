package com.antsyferov.astronerd.ui.panes.visualization

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.panes.planet_details.DetailsBottomSheet
import com.antsyferov.astronerd.ui.panes.settings.SettingsViewModel
import com.antsyferov.astronerd.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Visualization(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val isArEnabled by viewModel.flow.collectAsStateWithLifecycle(initialValue = false)

    val sheetState = rememberModalBottomSheetState()
    var isSheetVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

       Image(
           painter = painterResource(id = R.drawable.ursa),
           contentDescription = null,
           contentScale = ContentScale.Crop,
           modifier = Modifier.fillMaxSize()
       )

        var date by remember { mutableStateOf(LocalDateTime.now()) }
        var isPlaying by remember { mutableStateOf(true) }

        val speedOptions = remember { listOf(24, 30, 60, 100, 160, 240) }
        var selectedSpeed by remember { mutableIntStateOf(3) }
        val cameraPositions = CameraPosition.entries
        var selectedPositionIndex by remember { mutableIntStateOf(0) }
        val selectedPosition = cameraPositions[selectedPositionIndex]
        var clickedPlanet by remember {
            mutableStateOf(Planet.SUN)
        }

        LaunchedEffect(isPlaying) {
            if (isPlaying) {
                var lastFrameTimeNanos = 0L
                while (isActive) {
                    withFrameNanos { frameTimeNanos ->
                        if (lastFrameTimeNanos != 0L) {
                            val elapsedNanos = frameTimeNanos - lastFrameTimeNanos
                            val hours = speedOptions[selectedSpeed] * elapsedNanos / 1_000_000_000.0f
                            date = date.plusSeconds((hours * 60 * 60).toLong())
                        }
                        lastFrameTimeNanos = frameTimeNanos
                    }
                }
            }
        }

        if (isSheetVisible) {
            DetailsBottomSheet(
                sheetState = sheetState,
                planet = clickedPlanet,
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            isSheetVisible = false
                        }
                    }
                }
            )
        }

        if (isArEnabled) {
            ArVisualization(date)
        } else {
            Scene3D(
                date = date,
                onDateChanged = {
                    date = date.minusDays(it.toLong())
                },
                camera = selectedPosition,
                onChangeCameraToPlane = { planet ->
                    selectedPositionIndex = cameraPositions.indexOf(planet.toCameraPosition())
                },
                onShowDetails = {
                    clickedPlanet = it
                    isSheetVisible = true
                }
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentSize()
                .align(Alignment.TopCenter)
                .border(1.dp, Color.White, RoundedCornerShape(12.dp))
        ) {

            Text(
                text = "Speed: ${speedOptions[selectedSpeed]} hrs/s",
                style = AppTheme.typography.bold14,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = "Date: ${formatter.format(date)}",
                style = AppTheme.typography.bold14,
                color = Color.White,
                modifier = Modifier.padding(8.dp)

            )

        }


        Row(
            modifier = Modifier
                .padding(8.dp)
                .wrapContentSize()
                .align(Alignment.BottomCenter)
                .border(1.dp, Color.White, RoundedCornerShape(12.dp))

        ) {
            IconButton(onClick = {
                selectedSpeed = (selectedSpeed-1).coerceAtLeast(0)
            }) {
                Icon(
                    painterResource(id = R.drawable.ic_slower),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconToggleButton(
                checked = isPlaying,
                onCheckedChange = { isPlaying = it}
            ) {
                Icon(
                    painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(onClick = {
                selectedSpeed = (selectedSpeed+1).coerceAtMost(speedOptions.size-1)
            }) {
                Icon(
                    painterResource(id = R.drawable.ic_faster),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

        }

        if (!isArEnabled) {
            var isSelected by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp)
                    .background(Color.Black)
                    .border(1.dp, Color.White, RoundedCornerShape(12.dp))
                    .clickable { isSelected = !isSelected }
                    .animateContentSize()
            ) {
                Icon(
                    painter = painterResource(id = selectedPosition.icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp)
                )
                if (isSelected) {
                    for (position in cameraPositions - selectedPosition) {
                        Icon(
                            painter = painterResource(id = position.icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    selectedPositionIndex = cameraPositions.indexOf(position)
                                    isSelected = false
                                }
                        )
                    }
                }

            }
        }
    }
}