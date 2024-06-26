package com.antsyferov.astronerd.ui.panes.planet_details

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.panes.visualization.Planet
import com.antsyferov.astronerd.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
    sheetState: SheetState,
    planet: Planet,
    onDismiss: () -> Unit,
) {
    val resources by remember(planet) { mutableStateOf(getResourcesForPlanet(planet)) }
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        val scroll = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
                .verticalScroll(scroll)
        ) {
            Text(
                text = stringResource(id = resources.title),
                style = AppTheme.typography.bold22,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = resources.subtitle),
                style = AppTheme.typography.bold16,
                color = colorResource(id = resources.color)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text =stringResource(id = resources.body),
                style = AppTheme.typography.medium16
            )
        }
    }
}

fun getResourcesForPlanet(planet: Planet): PlanetResources {
    return when(planet) {
        Planet.SUN -> PlanetResources(R.string.title_sun, R.string.subtitle_sun, R.string.body_sun, R.color.sun)
        Planet.MERCURY -> PlanetResources(R.string.title_mercury, R.string.subtitle_mercury, R.string.body_mercury, R.color.mercury)
        Planet.VENUS -> PlanetResources(R.string.title_venus, R.string.subtitle_venus, R.string.body_venus, R.color.venus)
        Planet.EARTH -> PlanetResources(R.string.title_earth, R.string.subtitle_earth, R.string.body_earth, R.color.earth)
        Planet.MARS -> PlanetResources(R.string.title_mars, R.string.subtitle_mars, R.string.body_mars, R.color.mars)
        Planet.JUPITER -> PlanetResources(R.string.title_jupiter, R.string.subtitle_jupiter, R.string.body_jupiter, R.color.jupiter)
        Planet.SATURN -> PlanetResources(R.string.title_saturn, R.string.subtitle_saturn, R.string.body_saturn, R.color.saturn)
        Planet.URANUS -> PlanetResources(R.string.title_uranus, R.string.subtitle_uranus, R.string.body_uranus, R.color.uranus)
        Planet.NEPTUNE -> PlanetResources(R.string.title_neptune, R.string.subtitle_neptune, R.string.body_neptune, R.color.neptune)
    }
}

data class PlanetResources(
    @StringRes
    val title: Int,
    @StringRes
    val subtitle: Int,
    @StringRes
    val body: Int,
    @ColorRes
    val color: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DetailsBottomSheetPreview() {
    AppTheme {
        val state = rememberModalBottomSheetState()
        LaunchedEffect(key1 = Unit) {
            state.expand()
        }

        DetailsBottomSheet(state, Planet.SATURN) {}
    }
}