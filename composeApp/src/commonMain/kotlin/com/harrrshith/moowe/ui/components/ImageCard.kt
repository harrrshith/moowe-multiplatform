package com.harrrshith.moowe.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.utils.posterUrl

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ImageCard(
    modifier: Modifier,
    movieId: Int,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    imageUrl: String,
    onClick: () -> Unit,
) {
    val sharedKey = "movie-$movieId-trending"

    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .clickable(onClick = onClick)
        ) {
            // Main image with shared element transition
            Image(
                painter = rememberAsyncImagePainter(posterUrl(imageUrl)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = sharedKey),
                        animatedVisibilityScope = animatedContentScope,
                        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(24.dp)),
                        boundsTransform = { _, _ ->
                            spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium
                            )
                        }
                    )
            )

            Box(modifier = Modifier.fillMaxSize()) {
                ListingCardScrim(intensity = 0.95f)
            }
        }
    }
}
