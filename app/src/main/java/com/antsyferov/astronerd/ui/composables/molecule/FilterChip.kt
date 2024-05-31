package com.antsyferov.astronerd.ui.composables.molecule

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.ui.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String? = null,
    isAdd: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(end = 4.dp, top = 4.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick.invoke() }
            .background(AppTheme.colors.secondaryGray100)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .basicMarquee()

    ) {
        if (title != null) {
            Text(
                text = title,
                style = AppTheme.typography.semibold14,
                maxLines = 1
            )
        }
        if (value != null) {
            Text(
                text = value,
                style = AppTheme.typography.regular14,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 2.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
        }

        Icon(
            imageVector = if (isAdd) Icons.Default.Add else Icons.Default.Clear,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

    }
}