@file:OptIn(ExperimentalFoundationApi::class)

package com.harrrshith.moowe.ui.trending

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import coil3.compose.rememberAsyncImagePainter
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.SegmentedAppTopBar
import com.harrrshith.moowe.ui.theme.AppTheme
import com.harrrshith.moowe.utils.posterUrl
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TrendingRoute(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: TrendingViewModel = koinViewModel(),
    navigateToDetail: (Int, String, String, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val movies = viewModel.pagedMovies.collectAsLazyPagingItems()

    TrendingScreen(
        animatedContentScope = animatedContentScope,
        sharedTransitionScope = sharedTransitionScope,
        uiState = uiState,
        pagedMovies = movies,
        onMediaTypeSelected = viewModel::onMediaTypeChanged,
        onGenreSelected = viewModel::onGenreSelected,
        navigateToDetail = navigateToDetail,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun TrendingScreen(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    uiState: TrendingUiState,
    pagedMovies: LazyPagingItems<Movie>,
    onMediaTypeSelected: (com.harrrshith.moowe.domain.model.MediaType) -> Unit,
    onGenreSelected: (Genre) -> Unit,
    navigateToDetail: (Int, String, String, String) -> Unit,
) {
    val hazeState = LocalHazeState.current
    val gridState = rememberLazyStaggeredGridState()
    val scope = rememberCoroutineScope()

    fun changeMediaType(mediaType: com.harrrshith.moowe.domain.model.MediaType) {
        scope.launch {
            if (gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0) {
                gridState.animateScrollToItem(0)
            }
            onMediaTypeSelected(mediaType)
        }
    }

    fun changeGenre(genre: Genre) {
        scope.launch {
            if (gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0) {
                gridState.animateScrollToItem(0)
            }
            onGenreSelected(genre)
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SegmentedAppTopBar(
                    hazeState = hazeState,
                    selectedMediaType = uiState.selectedMediaType,
                    onMediaTypeSelected = ::changeMediaType,
                )
                GenreChipsRow(
                    genres = uiState.genres,
                    selectedGenre = uiState.selectedGenre,
                    onGenreSelected = ::changeGenre,
                )
            }
        },
    ) { innerPadding ->
        val isInitialLoading = pagedMovies.loadState.refresh is LoadState.Loading

        if (isInitialLoading && pagedMovies.itemCount == 0) {
            TrendingShimmerGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .hazeSource(hazeState),
            )
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                state = gridState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .hazeSource(hazeState),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 120.dp),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(
                    count = pagedMovies.itemCount,
                    key = { index -> "trend-${uiState.selectedMediaType.name}-${uiState.selectedGenre.id}-$index" },
                ) { index ->
                    val movie = pagedMovies[index]
                    if (movie == null) {
                        TrendingShimmerCell(index = index)
                        return@items
                    }
                    val sharedKey = "trending-${uiState.selectedGenre.id}-${uiState.selectedMediaType.name}-${movie.id}"
                    TrendingMovieCard(
                        movie = movie,
                        sharedKey = sharedKey,
                        animatedContentScope = animatedContentScope,
                        sharedTransitionScope = sharedTransitionScope,
                        onClick = {
                            navigateToDetail(movie.id, sharedKey, movie.title, movie.posterPath)
                        },
                    )
                }

            }
        }
    }
}

@Composable
private fun GenreChipsRow(
    genres: List<Genre>,
    selectedGenre: Genre,
    onGenreSelected: (Genre) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(genres, key = { it.name }) { genre ->
            val selected = genre == selectedGenre
            AssistChip(
                onClick = { onGenreSelected(genre) },
                label = {
                    Text(
                        text = genre.displayName,
                        style = AppTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected) AppTheme.colorScheme.primaryContainer else AppTheme.colorScheme.surfaceContainer,
                    labelColor = if (selected) AppTheme.colorScheme.onPrimaryContainer else AppTheme.colorScheme.onSurface,
                ),
                border = null,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun TrendingMovieCard(
    movie: Movie,
    sharedKey: String,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    onClick: () -> Unit,
) {
    val height = when (movie.id % 5) {
        0 -> 260.dp
        1 -> 220.dp
        2 -> 300.dp
        3 -> 240.dp
        else -> 280.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        with(sharedTransitionScope) {
            Image(
                painter = rememberAsyncImagePainter(posterUrl(movie.posterPath)),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = sharedKey),
                        animatedVisibilityScope = animatedContentScope,
                        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp)),
                        boundsTransform = { _, _ ->
                            spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMedium,
                            )
                        }
                    )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.42f),
                            Color.Black.copy(alpha = 0.74f),
                        )
                    )
                )
        )

        Text(
            text = movie.title,
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            color = Color.White,
            style = AppTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
        )
    }
}

@Composable
private fun TrendingShimmerGrid(
    modifier: Modifier = Modifier,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 120.dp),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(16) { index ->
            TrendingShimmerCell(index = index)
        }
    }
}

@Composable
private fun TrendingShimmerCell(index: Int) {
    val height = when (index % 5) {
        0 -> 260.dp
        1 -> 220.dp
        2 -> 300.dp
        3 -> 240.dp
        else -> 280.dp
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppTheme.colorScheme.surfaceContainer,
                        AppTheme.colorScheme.surfaceContainerHigh,
                    )
                )
            )
    )
}
