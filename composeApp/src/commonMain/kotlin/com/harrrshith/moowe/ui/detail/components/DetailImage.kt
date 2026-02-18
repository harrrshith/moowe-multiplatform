package com.harrrshith.moowe.ui.detail.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.Constants.IMAGE_BASE_URL
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.theme.AppTheme

fun LazyListScope.detailImage(
    movie: Movie,
    scrollOffset: Float,
    posterHeight: Dp,
    sharedKey: String,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(posterHeight)
        ) {
            with(sharedTransitionScope) {
                Image(
                    painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/${movie.posterPath}"),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationY = scrollOffset * 0.4f
                        }
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = sharedKey),
                            animatedVisibilityScope = animatedContentScope,
                            clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(0.dp)),
                            boundsTransform = { _, _ ->
                                spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessMediumLow
                                )
                            }
                        )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(posterHeight * 0.5f)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.6f),
                                AppTheme.colorScheme.surface
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
        }
    }
}