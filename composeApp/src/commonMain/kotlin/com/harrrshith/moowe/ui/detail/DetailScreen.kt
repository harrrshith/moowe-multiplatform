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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
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
    
    // Calculate poster height based on aspect ratio (2:3 for movie posters)
    val posterHeight = screenWidth * 1.4f // Show more of the poster
    
    // Top bar appears after scrolling past most of the poster
    val topBarStartThreshold = with(density) { (posterHeight - 120.dp).toPx() }
    val topBarTransitionRange = with(density) { 120.dp.toPx() }

    val scrollOffset by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == 0) {
                scrollState.firstVisibleItemScrollOffset.toFloat()
            } else {
                with(density) { (posterHeight + 100.dp).toPx() }
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

    Scaffold(
        topBar = {
            MooweTopAppBar(
                title = movie.title,
                alpha = topBarAlpha,
                onBackPressed = onBackPressed,
                onLikeClicked = { },
                onShareClicked = { }
            )
        },
        containerColor = AppTheme.colorScheme.surface
    ) { _ ->
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Hero Poster Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(posterHeight)
                ) {
                    // Poster image with parallax effect and shared element
                    with(sharedTransitionScope) {
                        Image(
                            painter = rememberAsyncImagePainter("$IMAGE_BASE_URL/${movie.posterPath}"),
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    translationY = scrollOffset * 0.4f // Parallax effect
                                }
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "movie-${movie.id}"),
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
                    
                    // Gradient overlay at bottom
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

            // Quick Info Section (Rating, Release Date)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Rating chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = AppTheme.colorScheme.primaryContainer
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "★",
                                    style = AppTheme.typography.titleMedium,
                                    color = AppTheme.colorScheme.primary
                                )
                                Text(
                                    text = movie.voteAverage.format(1),
                                    style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AppTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        
                        // Release year
                        Text(
                            text = movie.releaseDate.take(4), // Just the year
                            style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            color = AppTheme.colorScheme.onSurfaceVariant
                        )
                        
                        // Vote count
                        Text(
                            text = "${movie.voteCount} reviews",
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Overview section
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp)
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
                            lineHeight = 26.sp,
                            letterSpacing = 0.15.sp
                        ),
                        textAlign = TextAlign.Justify,
                        color = AppTheme.colorScheme.onSurface.copy(alpha = 0.87f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Movie details card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Details",
                            style = AppTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AppTheme.colorScheme.onSurface
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        DetailRow(label = "Release Date", value = movie.releaseDate)
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = AppTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                        
                        DetailRow(label = "Rating", value = "${movie.voteAverage}/10")
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = AppTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                        
                        DetailRow(label = "Popularity", value = movie.popularity.format(1))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
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
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = AppTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
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
    val iconBackgroundAlpha = (1f - alpha).coerceIn(0f, 0.7f)
    
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
                            color = Color.White
                        )
                    )
                }
            }
        },
        title = {
            if (alpha > 0.7f) {
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
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onLikeClicked(0) }) {
                    Image(
                        imageVector = LikeIcon,
                        contentDescription = "Like",
                        colorFilter = ColorFilter.tint(
                            color = Color.White
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
                    .background(Color.Black.copy(alpha = iconBackgroundAlpha)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = { onShareClicked(0) }) {
                    Image(
                        imageVector = ShareIcon,
                        contentDescription = "Share",
                        colorFilter = ColorFilter.tint(
                            color = Color.White
                        )
                    )
                }
            }
        }
    )
}
