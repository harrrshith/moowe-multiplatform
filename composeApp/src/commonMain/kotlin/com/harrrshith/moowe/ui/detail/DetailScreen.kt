@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
    viewModel: DetailScreenViewModel = koinViewModel(),
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

    Scaffold(
        topBar = {
            with(sharedTransitionScope) {
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
            }
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

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun DetailHeader(
    modifier: Modifier = Modifier,
    movie: Movie,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = movie.title,
                style = AppTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.3.sp,
                ),
                color = AppTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                HeaderPill(text = movie.releaseDate.take(4).ifBlank { "N/A" })
                HeaderPill(text = "${movie.voteAverage.format(1)}/10")
                if (movie.mediaType == MediaType.TV_SERIES && movie.numberOfSeasons != null) {
                    HeaderPill(text = "${movie.numberOfSeasons} seasons")
                }
            }
        }
    }
}

@Composable
private fun HeaderPill(text: String) {
    Surface(
        shape = RoundedCornerShape(100.dp),
        color = AppTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.wrapContentWidth(),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            text = text,
            style = AppTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = AppTheme.colorScheme.onSurfaceVariant,
        )
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
