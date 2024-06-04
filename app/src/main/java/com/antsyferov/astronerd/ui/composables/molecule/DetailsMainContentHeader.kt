package com.antsyferov.astronerd.ui.composables.molecule

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.composables.atom.animateClickable
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun DetailsMainContentHeader(
    modifier: Modifier = Modifier,
    name: String,
    id: String,
    sbdAction: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = AppTheme.typography.semibold24
        )
        Text(
            text = "ID: $id",
            style = AppTheme.typography.regular14,
            color = AppTheme.colors.secondaryGray600,
            modifier = Modifier.padding(top = 10.dp)
        )
        SbdChip(onClick = sbdAction, modifier = Modifier.padding(top = 32.dp))
    }
}

@Composable
private fun SbdChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, AppTheme.colors.secondaryGray700, CircleShape)
            .animateClickable(
                onClick = onClick,
                defaultColor = AppTheme.colors.background,
                pressedColor = AppTheme.colors.secondaryGray200
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_nasa),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text ="Go to SBD",
            style = AppTheme.typography.semibold14,
            modifier = Modifier.padding(
                start = 4.dp,
                end = 10.dp
            )
        )
    }
}