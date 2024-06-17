package com.antsyferov.astronerd.ui.composables.molecule

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun PrimaryFAB(
    modifier: Modifier = Modifier,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(6.dp),
    @DrawableRes iconRes: Int,
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick,
        elevation = elevation,
        containerColor = AppTheme.colors.primary,
        contentColor = AppTheme.colors.white,
        shape = CircleShape,
        modifier = modifier.size(60.dp)
    ) {
        Icon(
            painter =painterResource(id = iconRes) ,
            contentDescription = null,
            tint = AppTheme.colors.white,
            modifier = Modifier.size(30.dp)
        )
    }
}