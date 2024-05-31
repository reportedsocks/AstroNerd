package com.antsyferov.astronerd.ui.composables.template

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.ui.composables.atom.Shimmer
import com.antsyferov.astronerd.ui.theme.AppTheme

@Composable
fun AsteroidCardShimmer(
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, AppTheme.colors.border, RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {

        Column(Modifier.padding(16.dp)) {
            Row {
                Shimmer(
                    Modifier
                        .weight(0.6f)
                        .height(20.dp))
                Spacer(modifier = Modifier.weight(1f))
                Shimmer(
                    Modifier
                        .weight(0.2f)
                        .height(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Shimmer(
                    Modifier
                        .weight(0.3f)
                        .height(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Shimmer(
                    Modifier
                        .weight(0.3f)
                        .height(20.dp))
                Spacer(modifier = Modifier.weight(0.4f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Shimmer(
                    Modifier
                        .weight(0.7f)
                        .height(20.dp))
                Spacer(modifier = Modifier.weight(0.3f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Shimmer(
                    Modifier
                        .weight(0.9f)
                        .height(20.dp))
                Spacer(modifier = Modifier.weight(0.1f))
            }
        }

    }
}



@Preview
@Composable
private fun AsteroidCardPreview() {
    AsteroidCardShimmer(
    )
}