package com.antsyferov.astronerd.ui.composables.molecule

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.composables.OutlineIconButton
import com.antsyferov.astronerd.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(
    modifier: Modifier = Modifier,
    title: String,
    customAction: (@Composable (onBackClick: () -> Unit) -> Unit)? = null,
    onBackClick: () -> Unit,
    isFavorite: Boolean? = null,
    onFavoriteClick: ((value: Boolean) -> Unit)? = null
) {

    val haptic = LocalHapticFeedback.current

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = AppTheme.colors.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.semibold16
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colors.background
        ),
        modifier = modifier.padding(horizontal = 12.dp),
        navigationIcon = {
            OutlineIconButton(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                        modifier = Modifier.size(26.dp)
                    )
                },
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onBackClick()
                }
            )
        },
        actions = {
            if (isFavorite != null && onFavoriteClick != null) {
                if (customAction != null) {
                    customAction(onBackClick)
                } else {
                    OutlineIconButton(
                        icon = {
                            Icon(
                                painter = if (isFavorite) {
                                    painterResource(id = R.drawable.ic_bookmark_fill)
                                } else {
                                    painterResource(id = R.drawable.ic_bookmark)
                                },
                                contentDescription = null,
                                tint = AppTheme.colors.primary,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onFavoriteClick(!isFavorite)
                        }
                    )
                }
            }
        }
    )
}