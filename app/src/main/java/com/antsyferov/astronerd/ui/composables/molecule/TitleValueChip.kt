package com.antsyferov.astronerd.ui.composables.molecule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TitleValueChip(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AppTheme.colors.secondaryGray100)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .basicMarquee()
    ) {
        if (title != null) Text(
            text = "$title: ",
            style = AppTheme.typography.semibold14,
            maxLines = 1
        )
        Text(
            text = value,
            style = AppTheme.typography.regular14,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 2.dp)
        )
    }
}