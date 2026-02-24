package com.harrrshith.moowe.ui.discover

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.imagecarousel.ImageCarousel
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.ImageCard
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.posterUrl

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyListScope.trendingList(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    movies: List<Movie>,
    onClick: (Int, MediaType, String, String, String) -> Unit,
) {
    item {
        val itemWidthFraction = 0.6f
        val carouselWidth = screenWidth
        val sidePadding = (carouselWidth - (carouselWidth * itemWidthFraction)) / 2
        val density = LocalDensity.current
        val sidePaddingPx = with(density) { sidePadding.roundToPx() }

        val infiniteItemCount = Int.MAX_VALUE
        val startIndex = infiniteItemCount / 2
        val carouselState = rememberLazyListState(
            initialFirstVisibleItemIndex = startIndex,
            initialFirstVisibleItemScrollOffset = -sidePaddingPx
        )
        
        ImageCarousel(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            state = carouselState,
            itemHeight = screenWidth * 0.85f,
            itemWidthFraction = itemWidthFraction,
        ) {
            items(count = infiniteItemCount) { index ->
                val movieIndex = index % movies.size
                val movie = movies[movieIndex]
                ImageCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp)),
                    movieId = movie.id,
                    animatedContentScope = animatedContentScope,
                    sharedTransitionScope = sharedTransitionScope,
                    imageUrl = movie.posterPath,
                    onClick = {
                        onClick(
                            movie.id,
                            movie.mediaType,
                            "movie-${movie.id}-trending",
                            movie.title,
                            movie.posterPath,
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GenreMoviesSection(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    genre: Genre,
    movies: List<Movie>,
    itemsTobeDisplayed: Int,
    screenWidth: Dp,
    onSectionComposed: (Genre) -> Unit = {},
    onClick: (Int, MediaType, String, String, String) -> Unit = { _, _, _, _, _ -> },
) {
    LaunchedEffect(genre) {
        onSectionComposed(genre)
    }

    val listState = rememberSaveable(genre, saver = LazyListState.Saver) {
        LazyListState()
    }

    val actualItemWidth = remember { screenWidth / (itemsTobeDisplayed + 0.5f) }
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = genre.displayName,
            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = movies, key = { movie -> movie.id }) { movie ->
                Box(
                    modifier = Modifier
                        .width(actualItemWidth)
                        .aspectRatio(0.75f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            onClick(
                                movie.id,
                                movie.mediaType,
                                "movie-${movie.id}-${genre.id}",
                                movie.title,
                                movie.posterPath,
                            )
                        }
                ) {
                    with(sharedTransitionScope) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray.copy(alpha = 0.5f))
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "movie-${movie.id}-${genre.id}"),
                                    animatedVisibilityScope = animatedContentScope,
                                    clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(12.dp)),
                                    boundsTransform = { _, _ ->
                                        spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessMedium
                                        )
                                    }
                                ),
                            painter = rememberAsyncImagePainter(posterUrl(movie.posterPath)),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GenreShimmerSection(
    genre: Genre,
    screenWidth: Dp,
    itemsTobeDisplayed: Int,
    onSectionComposed: (Genre) -> Unit,
) {
    LaunchedEffect(genre) {
        onSectionComposed(genre)
    }

    val actualItemWidth = remember { screenWidth / (itemsTobeDisplayed + 0.5f) }
    val shimmerBrush = rememberShimmerBrush()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = genre.displayName,
            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(itemsTobeDisplayed) {
                Box(
                    modifier = Modifier
                        .width(actualItemWidth)
                        .aspectRatio(0.75f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(shimmerBrush),
                )
            }
        }
    }
}

fun LazyListScope.trendingShimmerList() {
    item(key = "trending-shimmer") {
        val shimmerBrush = rememberShimmerBrush()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(screenWidth * 0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(shimmerBrush),
        )
    }
}

@Composable
private fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val xShift = transition.animateFloat(
        initialValue = -600f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(animation = tween(1150, easing = LinearEasing)),
        label = "shimmerX",
    ).value

    return Brush.linearGradient(
        colors = listOf(
            AppTheme.colorScheme.surfaceContainer.copy(alpha = 0.55f),
            AppTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f),
            AppTheme.colorScheme.surfaceContainer.copy(alpha = 0.55f),
        ),
        start = Offset(xShift - 420f, 0f),
        end = Offset(xShift, 380f),
    )
}
