package com.antsyferov.astronerd.ui.composables.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.composables.atom.OutlineBox
import com.antsyferov.astronerd.ui.composables.template.DetailsTableItem

@Composable
fun DetailsCompareDistanceChart(
    astronomicalDistance: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CompareDistanceChart(
            astronomicalDistance = astronomicalDistance
        )
        OutlineBox(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            DetailsTableItem(
                title = stringResource(R.string.details_chart_title_astronomical_distance),
                itemValue = "$astronomicalDistance ${stringResource(R.string.unit_astronomical)}",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}