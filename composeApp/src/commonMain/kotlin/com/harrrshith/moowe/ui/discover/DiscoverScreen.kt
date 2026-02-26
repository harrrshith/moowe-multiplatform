@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.discover

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.SegmentedAppTopBar
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverRoute(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: DiscoverViewModel = koinViewModel(),
    navigateToSearch: () -> Unit,
    navigateToDetail: (Int, MediaType, String, String, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeState.current

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is DiscoverUiEvent.NavigateToDetail -> {
                    navigateToDetail(event.id, event.mediaType, event.sharedKey, event.title, event.posterPath)
                }
                is DiscoverUiEvent.ShowError -> {}
            }
        }
    }

    DiscoverScreen(
        modifier = Modifier.fillMaxSize(),
        animatedContentScope = animatedContentScope,
        sharedTransitionScope = sharedTransitionScope,
        hazeState = hazeState,
        selectedMediaType = uiState.selectedMediaType,
        trendingMovies = uiState.trendingMovies,
        isTrendingLoading = uiState.isTrendingLoading,
        genreOrder = uiState.genreOrder,
        genreMovies = uiState.genreMovies,
        loadingGenres = uiState.loadingGenres,
        loadedGenres = uiState.loadedGenres,
        isRefreshing = uiState.isRefreshing,
        onGenreVisible = viewModel::onGenreVisible,
        onMediaTypeChanged = viewModel::onMediaTypeChanged,
        navigateToSearch = navigateToSearch,
        onRefresh = viewModel::onRefresh,
        onClick = { id, mediaType, sharedKey, title, posterPath ->
            viewModel.onMovieClick(id, mediaType, sharedKey, title, posterPath)
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    hazeState: HazeState,
    selectedMediaType: MediaType,
    trendingMovies: List<Movie>? = null,
    isTrendingLoading: Boolean = false,
    genreOrder: List<Genre> = emptyList(),
    genreMovies: Map<Genre, List<Movie>> = emptyMap(),
    loadingGenres: Set<Genre> = emptySet(),
    loadedGenres: Set<Genre> = emptySet(),
    isRefreshing: Boolean = false,
    onGenreVisible: (Genre) -> Unit,
    onMediaTypeChanged: (MediaType) -> Unit,
    navigateToSearch: () -> Unit,
    onRefresh: () -> Unit = {},
    onClick: (Int, MediaType, String, String, String) -> Unit
) {
    val width = screenWidth
    Scaffold(
        modifier = modifier,
        topBar = {
            SegmentedAppTopBar(
                hazeState = hazeState,
                selectedMediaType = selectedMediaType,
                onMediaTypeSelected = onMediaTypeChanged,
                onSearchClick = navigateToSearch,
            )
        }
    ) { innerPadding ->
        val pullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize(),
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState,
        ) {
            Crossfade(
                targetState = selectedMediaType,
                animationSpec = tween(durationMillis = 400, easing = EaseInOut),
                modifier = Modifier.fillMaxSize(),
                label = "media_type_content_transition"
            ) { _ ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(state = hazeState),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding()
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when {
                        isTrendingLoading -> {
                            trendingShimmerList()
                        }

                        !trendingMovies.isNullOrEmpty() -> {
                            trendingList(
                                animatedContentScope = animatedContentScope,
                                sharedTransitionScope = sharedTransitionScope,
                                movies = trendingMovies,
                                onClick = { id, mediaType, sharedKey, title, posterPath ->
                                    onClick(id, mediaType, sharedKey, title, posterPath)
                                }
                            )
                        }
                    }

                    itemsIndexed(
                        items = genreOrder,
                        key = { _, genre -> genre.name }
                    ) { _, genre ->
                        val movies = genreMovies[genre].orEmpty()
                        val isLoadingGenre = genre in loadingGenres
                        val isLoadedGenre = genre in loadedGenres

                        when {
                            movies.isNotEmpty() -> {
                                GenreMoviesSection(
                                    animatedContentScope = animatedContentScope,
                                    sharedTransitionScope = sharedTransitionScope,
                                    genre = genre,
                                    movies = movies,
                                    itemsTobeDisplayed = 3,
                                    screenWidth = width,
                                    onSectionComposed = onGenreVisible,
                                    onClick = { id, mediaType, sharedKey, title, posterPath ->
                                        onClick(id, mediaType, sharedKey, title, posterPath)
                                    }
                                )
                            }

                            !isLoadedGenre || isLoadingGenre -> {
                                GenreShimmerSection(
                                    genre = genre,
                                    screenWidth = width,
                                    itemsTobeDisplayed = 3,
                                    onSectionComposed = onGenreVisible,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// Preview disabled due to shared element transitions requiring navigation scope
// @Preview()
// @Composable
// private fun DiscoverScreenPreview() {
//     val fakeHazeState = rememberHazeState()
//     DiscoverScreen(
//         hazeState = fakeHazeState,
//         trendingMovies = mockMovies,
//         actionMovies = mockMovies,
//         onClick = { }
//     )
// }
