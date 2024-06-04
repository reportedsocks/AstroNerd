package com.antsyferov.astronerd.ui.composables.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.composables.atom.OutlineBox
import com.antsyferov.impl.local.AsteroidEntity

@Composable
fun DetailsTable(
    modifier: Modifier = Modifier,
    data: AsteroidEntity
) {

    val closeApproachData = data.closeApproachData!!//[0] // This list contains only one item with most closest date

    val missDistanceUnit = closeApproachData.missDistance
    val relativeVelocity = closeApproachData.relativeVelocity
    val diameterUnits = data.estimatedDiameter.kilometers

    //val diameterPrefixUnit = userPrefs?.diameterUnits?.unit?.let { stringResource(id = it) } ?: "N/A"
    //val relativeVelocityPrefix = userPrefs?.relativeVelocityUnits?.unit?.let { stringResource(id = it) } ?: "N/A"
    //val missDistancePrefix = userPrefs?.missDistanceUnits?.unit?.let { stringResource(id = it) } ?: "N/A"

    OutlineBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            DetailsTableItem(
                title = stringResource(R.string.details_table_item_dangerous),
                itemValue = data.isPotentiallyHazardousAsteroid.toString(),
                isDangerItem = true
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_min_diameter),
                itemValue = diameterUnits.min.toString()
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_max_diameter),
                itemValue = diameterUnits.max.toString()
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_close_approach),
                itemValue = closeApproachData.closeApproachDateFull
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_relative_velocity),
                itemValue = relativeVelocity.kilometersPerHour
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_miss_distance),
                itemValue = missDistanceUnit.kilometers
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_orbiting_body),
                itemValue = closeApproachData.orbitingBody
            )

            DetailsTableItem(
                title = stringResource(R.string.details_table_item_is_sentry_object),
                itemValue = data.isSentryObject.toString(),
                isSentryItem = true
            )
        }
    }
}