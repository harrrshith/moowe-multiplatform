@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.detail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.imagecarousel.utils.screenHeight
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.Constants.IMAGE_BASE_URL
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.composeVectors.ArrowBackIcon
import com.harrrshith.moowe.ui.components.composeVectors.LikeIcon
import com.harrrshith.moowe.ui.components.composeVectors.ShareIcon
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.extensions.format
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailRoute(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: DetailScreenViewModel = koinViewModel(),
    onBackPressed: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    uiState.movie?.let { movie ->
        DetailScreen(
            animatedContentScope = animatedContentScope,
            sharedTransitionScope = sharedTransitionScope,
            movie = movie,
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailScreen(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    movie: Movie,
    onBackPressed: () -> Unit
){
    val scrollState = rememberLazyListState()
    val density = LocalDensity.current
    
    val backdropHeight = screenHeight * 0.35f
    val posterWidth = screenWidth * 0.35f
    val posterHeight = posterWidth * 1.5f
    val posterTopOffset = backdropHeight - (posterHeight * 0.4f)
    
    // Top bar should only start appearing after scrolling most of the backdrop
    val topBarStartThreshold = with(density) { (backdropHeight - 100.dp).toPx() }
    val topBarTransitionRange = with(density) { 100.dp.toPx() }

    val scrollOffset by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == 0) {
                scrollState.firstVisibleItemScrollOffset.toFloat()
            } else {
                with(density) { (backdropHeight + 100.dp).toPx() }
            }
        }
    }

    val topBarAlpha by remember {
        derivedStateOf {
            when {
                scrollState.firstVisibleItemIndex > 0 -> 1f
                scrollOffset < topBarStartThreshold -> 0f
                else -> ((scrollOffset - topBarStartThreshold) / topBarTransitionRange).coerceIn(0f, 1f)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(backdropHeight + posterHeight * 0.6f)
                ) {
                    // Backdrop image with parallax
                    Image(
                        painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/${movie.backdropPath}"),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(backdropHeight)
                            .graphicsLayer {
                                translationY = scrollOffset * 0.5f
                            }
                    )
                    
                    // Gradient overlay on backdrop
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(backdropHeight)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f),
                                        AppTheme.colorScheme.surface.copy(alpha = 0.95f)
                                    ),
                                    startY = 0f,
                                    endY = with(density) { backdropHeight.toPx() }
                                )
                            )
                    )

                    // Poster image with shared element
                    with(sharedTransitionScope) {
                        Card(
                            modifier = Modifier
                                .width(posterWidth)
                                .height(posterHeight)
                                .offset(x = 24.dp, y = posterTopOffset),
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/${movie.posterPath}"),
                                contentDescription = movie.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "movie-${movie.id}"),
                                        animatedVisibilityScope = animatedContentScope,
                                        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(12.dp)),
                                        boundsTransform = { _, _ ->
                                            spring(
                                                dampingRatio = Spring.DampingRatioNoBouncy,
                                                stiffness = Spring.StiffnessMediumLow
                                            )
                                        }
                                    )
                            )
                        }
                    }
                }
            }

            // Movie title and metadata
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = posterWidth + 48.dp, end = 24.dp, top = 16.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = AppTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        ),
                        color = AppTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AppTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "★",
                                style = AppTheme.typography.titleMedium,
                                color = AppTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = movie.voteAverage.format(1),
                            style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "(${movie.voteCount} votes)",
                            style = AppTheme.typography.bodySmall,
                            color = AppTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Release date
                    Text(
                        text = "Released ${movie.releaseDate}",
                        style = AppTheme.typography.bodyMedium,
                        color = AppTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Overview section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                        Text(
                            text = "Overview",
                            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = movie.overview,
                            style = AppTheme.typography.bodyLarge.copy(
                                lineHeight = 24.sp,
                                letterSpacing = 0.15.sp
                            ),
                            color = AppTheme.colorScheme.onSurface.copy(alpha = 0.87f)
                        )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Movie details card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colorScheme.surfaceContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Details",
                            style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        DetailRow(label = "Popularity", value = movie.popularity.format(1))
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = AppTheme.colorScheme.outlineVariant
                        )
                        
                        DetailRow(label = "Release Date", value = movie.releaseDate)
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = AppTheme.colorScheme.outlineVariant
                        )
                        
                        DetailRow(label = "Rating", value = "${movie.voteAverage}/10")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Top app bar
        MooweTopAppBar(
            title = movie.title,
            alpha = topBarAlpha,
            onBackPressed = onBackPressed,
            onLikeClicked = { },
            onShareClicked = { }
        )
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = AppTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun MooweTopAppBar(
    title: String,
    alpha: Float,
    onBackPressed: () -> Unit,
    onLikeClicked: (Int) -> Unit,
    onShareClicked: (Int) -> Unit
) {
    // Icon background appears when top bar is transparent for better visibility
    val iconBackgroundAlpha = (1f - alpha).coerceIn(0f, 0.6f)
    
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppTheme.colorScheme.surface.copy(alpha),
        ),
        navigationIcon = {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBackPressed) {
                    Image(
                        imageVector = ArrowBackIcon,
                        contentDescription = "Back",
                        colorFilter = ColorFilter.tint(
                            color = if (alpha < 0.5f) Color.White else AppTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        },
        title = {
            if (alpha > 0.7f) {
                Text(
                    text = title,
                    style = AppTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onLikeClicked(0) }) {
                    Image(
                        imageVector = LikeIcon,
                        contentDescription = "Like",
                        colorFilter = ColorFilter.tint(
                            color = if (alpha < 0.5f) Color.White else AppTheme.colorScheme.onSurface
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onShareClicked(0) }) {
                    Image(
                        imageVector = ShareIcon,
                        contentDescription = "Share",
                        colorFilter = ColorFilter.tint(
                            color = if (alpha < 0.5f) Color.White else AppTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        }
    )
}

// Preview disabled due to shared element transitions requiring navigation scope
// @Preview
// @Composable
// fun PreviewDetailScreen() {
//     AppTheme {
//         Surface {
//             DetailScreen(
//                 movie = mockMovie,
//                 onBackPressed = { }
//             )
//         }
//     }
// }