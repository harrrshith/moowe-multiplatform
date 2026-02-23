package com.harrrshith.moowe.ui.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.harrrshith.moowe.ui.components.composeVectors.ArrowBackIcon
import com.harrrshith.moowe.ui.components.composeVectors.LikeIcon
import com.harrrshith.moowe.ui.components.composeVectors.ShareIcon
import com.harrrshith.moowe.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    title: String,
    collapseProgress: Float,
    onBackPressed: () -> Unit,
    onLikeClicked: (Int) -> Unit,
    onShareClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = collapseProgress.coerceIn(0f, 1f)
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 160),
        label = "topBarProgress",
    ).value

    val containerColor = lerp(
        start = Color.Transparent,
        stop = AppTheme.colorScheme.surface,
        fraction = animatedProgress,
    )
    val iconContainerColor = lerp(
        start = AppTheme.colorScheme.surface.copy(alpha = 0.32f),
        stop = AppTheme.colorScheme.surfaceContainerHigh,
        fraction = animatedProgress,
    )
    val iconTint = lerp(
        start = Color.White,
        stop = AppTheme.colorScheme.onSurface,
        fraction = animatedProgress,
    )

    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
        ),
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBackPressed) {
                    Image(
                        imageVector = ArrowBackIcon,
                        contentDescription = "Back",
                        colorFilter = ColorFilter.tint(
                            color = iconTint
                        )
                    )
                }
            }
        },
        title = {
            AnimatedVisibility(
                visible = animatedProgress > 0.64f,
                enter = fadeIn(tween(140)) + slideInHorizontally(tween(180)) { it / 4 },
                exit = fadeOut(tween(110)) + slideOutHorizontally(tween(110)) { it / 6 },
            ) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    color = AppTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onLikeClicked(0) }) {
                    Image(
                        imageVector = LikeIcon,
                        contentDescription = "Like",
                        colorFilter = ColorFilter.tint(
                            color = iconTint
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onShareClicked(0) }) {
                    Image(
                        imageVector = ShareIcon,
                        contentDescription = "Share",
                        colorFilter = ColorFilter.tint(
                            color = iconTint
                        )
                    )
                }
            }
        }
    )
}
