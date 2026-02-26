@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.domain.model.CastMember
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.model.Review
import com.harrrshith.moowe.ui.detail.components.detailCast
import com.harrrshith.moowe.ui.detail.components.DetailTopAppBar
import com.harrrshith.moowe.ui.detail.components.detailImage
import com.harrrshith.moowe.ui.detail.components.detailLineTwo
import com.harrrshith.moowe.ui.detail.components.detailOverview
import com.harrrshith.moowe.ui.detail.components.detailRelatedMovies
import com.harrrshith.moowe.ui.detail.components.detailReviews
import com.harrrshith.moowe.ui.detail.components.detailSeasons
import com.harrrshith.moowe.ui.detail.mock.mockMovie
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.extensions.format
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailRoute(
    sharedKey: String,
    initialTitle: String,
    initialPosterPath: String,
    mediaType: MediaType,
    movieId: Int,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: DetailScreenViewModel = koinViewModel(
        key = "detail-$movieId-${mediaType.apiValue}",
        parameters = { parametersOf(movieId, mediaType) }
    ),
    onBackPressed: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movie = uiState.movie ?: Movie(
        id = movieId,
        title = initialTitle,
        overview = "",
        posterPath = initialPosterPath,
        backdropPath = "",
        releaseDate = "",
        voteAverage = 0.0,
        voteCount = 0,
        popularity = 0.0,
        adult = false,
        genreIds = emptyList(),
        mediaType = mediaType,
    )

    DetailScreen(
        sharedKey = sharedKey,
        animatedContentScope = animatedContentScope,
        sharedTransitionScope = sharedTransitionScope,
        movie = movie,
        reviews = uiState.reviews,
        cast = uiState.cast,
        relatedMovies = uiState.relatedMovies,
        isLiked = uiState.isLiked,
        onLikeClicked = viewModel::onLikeClicked,
        onBackPressed = onBackPressed
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailScreen(
    sharedKey: String,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    movie: Movie,
    reviews: List<Review> = emptyList(),
    cast: List<CastMember> = emptyList(),
    relatedMovies: List<Movie> = emptyList(),
    isLiked: Boolean,
    onLikeClicked: () -> Unit,
    onBackPressed: () -> Unit
){
    val scrollState = rememberLazyListState()
    val density = LocalDensity.current
    val posterHeight = screenWidth * 1.4f
    val topBarStartThreshold = with(density) { (posterHeight * 0.35f).toPx() }
    val topBarTransitionRange = with(density) { (posterHeight * 0.25f).toPx() }

    val scrollOffset by remember {
        derivedStateOf {
            if (scrollState.firstVisibleItemIndex == 0) {
                scrollState.firstVisibleItemScrollOffset.toFloat()
            } else {
                with(density) { (posterHeight + 100.dp).toPx() }
            }
        }
    }

    val collapseProgress by remember {
        derivedStateOf {
            when {
                scrollState.firstVisibleItemIndex > 0 -> 1f
                scrollOffset < topBarStartThreshold -> 0f
                else -> ((scrollOffset - topBarStartThreshold) / topBarTransitionRange).coerceIn(0f, 1f)
            }
        }
    }

    with(sharedTransitionScope) {
        val isTransitionActiveNow = isTransitionActive

        Scaffold(
            topBar = {
                DetailTopAppBar(
                    title = movie.title,
                    collapseProgress = collapseProgress,
                    onBackPressed = onBackPressed,
                    isLiked = isLiked,
                    onLikeClicked = onLikeClicked,
                    onShareClicked = { },
                    modifier = Modifier.renderInSharedTransitionScopeOverlay(
                        renderInOverlay = {
                            isTransitionActive &&
                            animatedContentScope.transition.targetState == EnterExitState.Visible
                        },
                        zIndexInOverlay = 2f,
                    ),
                )
            },
            containerColor = AppTheme.colorScheme.surface
        ) { _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = scrollState
        ) {
            detailImage(
                movie = movie,
                scrollOffset = scrollOffset,
                posterHeight = posterHeight,
                sharedKey = sharedKey,
                animatedContentScope = animatedContentScope,
                sharedTransitionScope = sharedTransitionScope
            )

            item {
                DetailHeader(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    movie = movie,
                )
            }

            detailLineTwo(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                movie = movie
            )


            detailOverview(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                movie = movie
            )

            if (!isTransitionActiveNow) {
                detailCast(
                    modifier = Modifier.padding(top = 8.dp),
                    cast = cast,
                )

                detailRelatedMovies(
                    modifier = Modifier.padding(top = 22.dp),
                    relatedMovies = relatedMovies,
                    mediaType = movie.mediaType,
                )

                detailSeasons(
                    modifier = Modifier.padding(top = 22.dp),
                    seasons = movie.seasons,
                )

                detailReviews(
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
                    reviews = reviews,
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
        }
    }
}

@Composable
private fun DetailHeader(
    modifier: Modifier = Modifier,
    movie: Movie,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AppTheme.colorScheme.primaryContainer.copy(alpha = 0.22f),
                            AppTheme.colorScheme.surfaceContainerLow.copy(alpha = 0.72f),
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = AppTheme.colorScheme.surface.copy(alpha = 0.58f))
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = movie.title,
                    style = AppTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                    ),
                    color = AppTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GlassPill(
                        icon = "📅",
                        text = movie.releaseDate.take(4).ifBlank { "N/A" },
                        gradient = listOf(
                            AppTheme.colorScheme.tertiaryContainer.copy(alpha = 0.32f),
                            AppTheme.colorScheme.tertiaryContainer.copy(alpha = 0.16f),
                        )
                    )
                    GlassPill(
                        icon = "⭐",
                        text = movie.voteAverage.format(1),
                        gradient = listOf(
                            AppTheme.colorScheme.primaryContainer.copy(alpha = 0.32f),
                            AppTheme.colorScheme.primaryContainer.copy(alpha = 0.16f),
                        )
                    )
                    if (movie.mediaType == MediaType.TV_SERIES && movie.numberOfSeasons != null) {
                        GlassPill(
                            icon = "📺",
                            text = "${movie.numberOfSeasons} S",
                            gradient = listOf(
                                AppTheme.colorScheme.secondaryContainer.copy(alpha = 0.32f),
                                AppTheme.colorScheme.secondaryContainer.copy(alpha = 0.16f),
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassPill(
    icon: String,
    text: String,
    gradient: List<Color>,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(brush = Brush.linearGradient(colors = gradient))
    ) {
        Row(
            modifier = Modifier
                .background(color = AppTheme.colorScheme.surface.copy(alpha = 0.54f))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = icon,
                style = AppTheme.typography.labelMedium,
                fontSize = 14.sp,
            )

            Text(
                text = text,
                style = AppTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.2.sp,
                ),
                color = AppTheme.colorScheme.onSurface,
            )
        }
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    AppTheme {
        Surface {
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = mockMovie,
                    label = "preview"
                ) { movie ->
                    DetailScreen(
                        sharedKey = "movie-${movie.id}",
                        animatedContentScope = this,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        movie = movie,
                        isLiked = false,
                        onLikeClicked = {},
                        onBackPressed = {}
                    )
                }
            }
        }
    }
}
