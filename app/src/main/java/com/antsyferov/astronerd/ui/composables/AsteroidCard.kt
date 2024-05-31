package com.antsyferov.astronerd.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.UnitUtils
import com.antsyferov.astronerd.ui.composables.molecule.TitleValueChip
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun AsteroidCard(
    modifier: Modifier = Modifier,
    name: String,
    isDangerous: Boolean,
    diameterMin: Double,
    diameterMax: Double,
    diameterUnits: String,
    orbitingBody: String,
    closeApproach: String,
    onCardClick: () -> Unit,
    onDeleteAsteroid: (() -> Unit)? = null
) {

    val reformattedDiameterMin = "${UnitUtils.roundDouble(diameterMin)} $diameterUnits"
    val reformattedDiameterMax = "${UnitUtils.roundDouble(diameterMax)} $diameterUnits"

    val diametersData = listOf(
        Pair("Min", reformattedDiameterMin),
        Pair("Max", reformattedDiameterMax)
    )

    val otherData = listOf(
        Pair("Orbiting Body", orbitingBody),
        Pair("Close Approach", closeApproach)
    )

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, AppTheme.colors.border, RoundedCornerShape(12.dp))
            .clickable(onClick = onCardClick)
    ) {
        Column(
        ) {
            MainInfo(name = name, isDangerous = isDangerous)
            SecondaryInfo(
                title = "Diameters",
                data = diametersData,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SecondaryInfo(
                title = "Other",
                data = otherData,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp,
                    end = 64.dp
                )
            )
        }

        if (onDeleteAsteroid != null) {
            OutlineIconButton(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = null
                    )
                },
                onClick = onDeleteAsteroid,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SecondaryInfo(
    modifier: Modifier = Modifier,
    title: String,
    data: List<Pair<String, String>>
) {

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = AppTheme.colors.outline
        )
        FlowRow(
            modifier = Modifier.padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            data.forEach { pair ->
                TitleValueChip(
                    title = pair.first,
                    value = pair.second
                )
            }
        }
    }
}

@Composable
fun MainInfo(
    name: String,
    isDangerous: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = AppTheme.typography.semibold16,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IsDangerous(
            isDangerous = isDangerous
        )
    }
}

@Composable
private fun IsDangerous(
    modifier: Modifier = Modifier,
    isDangerous: Boolean
) {

    val containerColor =
        if (isDangerous) AppTheme.colors.secondaryRed100 else AppTheme.colors.secondaryGreen100

    val textColor =
        if (isDangerous) AppTheme.colors.secondaryRed else AppTheme.colors.secondaryGreen

    val textResult = if (isDangerous) "Yes" else "No"

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Danger: ",
            style = AppTheme.typography.bold14,
            color = textColor
        )
        Text(
            text = textResult,
            style = AppTheme.typography.regular14,
            modifier = Modifier.padding(start = 2.dp),
            color = textColor
        )
    }
}

@Preview
@Composable
private fun AsteroidCardPreview() {
    AsteroidCard(
        diameterUnits = "Km",
        name = "347813 (2002 NP1)",
        isDangerous = true,
        diameterMin = 0.7808272775,
        diameterMax = 1.7459828712,
        orbitingBody = "Earth",
        closeApproach = "2021-08-13",
        onCardClick = { }
    )
}