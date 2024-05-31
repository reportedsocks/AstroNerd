package com.antsyferov.astronerd.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun OutlineIconButton(
    modifier: Modifier = Modifier,
    icon: @Composable BoxScope.() -> Unit,
    onClick: () -> Unit
) {

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.dp, AppTheme.colors.secondaryGray200, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = icon
    )
}