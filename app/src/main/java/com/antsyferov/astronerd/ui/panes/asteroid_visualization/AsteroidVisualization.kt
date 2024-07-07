package com.antsyferov.astronerd.ui.panes.asteroid_visualization

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.panes.settings.SettingsViewModel
import com.antsyferov.astronerd.ui.theme.AppTheme
import kotlinx.coroutines.isActive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


@Composable
fun AsteroidVisualization(
    asteroidId: String,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val isArEnabled by viewModel.flow.collectAsStateWithLifecycle(initialValue = false)
    val asteroid by viewModel.asteroidFlow.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(asteroidId) {
        viewModel.getAsteroid(asteroidId)
    }


    val context = LocalContext.current

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

        val speedOptions = remember { listOf(60, 120, 240, 300) }
        var selectedSpeed by remember { mutableIntStateOf(2) }


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



        asteroid?.let {
            if (isArEnabled) {
                AsteroidSceneAR(date = date, asteroidEntity = it)
            } else {
                AsteroidScene3D(
                    date = date,
                    onDateChanged = {
                        date = date.minusDays(it.toLong())
                    },
                    asteroidEntity = it
                )
            }
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

    }
}