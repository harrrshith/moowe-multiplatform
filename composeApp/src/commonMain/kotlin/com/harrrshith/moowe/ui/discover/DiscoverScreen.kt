@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrrshith.imagecarousel.utils.screenWidth
import com.harrrshith.moowe.LocalHazeState
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.ui.components.AppTopBar
import com.harrrshith.moowe.ui.theme.AppTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverRoute(
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    viewModel: DiscoverViewModel = koinViewModel(),
    navigateToDetail: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeState.current

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is DiscoverUiEvent.NavigateToDetail -> {
                    navigateToDetail(event.id)
                }
                is DiscoverUiEvent.ShowError -> {}
            }
        }
    }

    Crossfade(uiState.isLoading) { isLoading ->
        when(isLoading) {
            true -> {
                LoadingScreen()
            } else -> {
                DiscoverScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    animatedContentScope = animatedContentScope,
                    sharedTransitionScope = sharedTransitionScope,
                    hazeState = hazeState,
                    selectedMediaType = uiState.selectedMediaType,
                    trendingMovies = uiState.trendingMovies,
                    actionMovies = uiState.actionMovies,
                    adventureMovies = uiState.adventureMovies,
                    fantasyMovies = uiState.fantasyMovies,
                    documentaries = uiState.documentaries,
                    isRefreshing = uiState.isRefreshing,
                    onMediaTypeChanged = viewModel::onMediaTypeChanged,
                    onRefresh = viewModel::onRefresh,
                    onClick = viewModel::onMovieClick
                )
            }
        }
    }

}
@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
                .height(48.dp),
            color = AppTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverScreen(
    modifier: Modifier = Modifier,
    animatedContentScope: AnimatedContentScope,
    sharedTransitionScope: SharedTransitionScope,
    hazeState: HazeState,
    selectedMediaType: com.harrrshith.moowe.domain.model.MediaType,
    trendingMovies: List<Movie>? = null,
    actionMovies: List<Movie>? = null,
    adventureMovies: List<Movie>? = null,
    fantasyMovies: List<Movie>? = null,
    documentaries: List<Movie>? = null,
    isRefreshing: Boolean = false,
    onMediaTypeChanged: (com.harrrshith.moowe.domain.model.MediaType) -> Unit,
    onRefresh: () -> Unit = {},
    onClick: (Int) -> Unit
) {
    val width = screenWidth
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "moowe",
                hazeState = hazeState,
                selectedMediaType = selectedMediaType,
                onMediaTypeSelected = onMediaTypeChanged
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
            AnimatedContent(
                targetState = selectedMediaType,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    (fadeIn(animationSpec = tween(durationMillis = 300)) +
                        slideInVertically(
                            animationSpec = tween(durationMillis = 300),
                            initialOffsetY = { fullHeight -> fullHeight / 12 }
                        )).togetherWith(
                        fadeOut(animationSpec = tween(durationMillis = 200))
                    )
                },
                label = "media_type_content_transition"
            ) { _ ->
                val actionListState = rememberLazyListState()
                val adventureListState = rememberLazyListState()
                val romanceListState = rememberLazyListState()
                val documentaryListState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .hazeSource(state = hazeState),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = 120.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    trendingMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                        trendingList(
                            animatedContentScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            movies = movies,
                            onClick = { id ->
                                onClick(id)
                            }
                        )
                    }

                    actionMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                        movieList(
                            animatedContentScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            genre = Genre.ACTION,
                            movies = movies,
                            lazyListState = actionListState,
                            itemsTobeDisplayed = 3,
                            screenWidth = width,
                            onClick = { id ->
                                onClick(id)
                            }
                        )
                    }

                    adventureMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                        movieList(
                            animatedContentScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            genre = Genre.ADVENTURE,
                            movies = movies,
                            lazyListState = adventureListState,
                            itemsTobeDisplayed = 3,
                            screenWidth = width,
                            onClick = { id ->
                                onClick(id)
                            }
                        )
                    }

                    fantasyMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                        movieList(
                            animatedContentScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            genre = Genre.FANTASY,
                            movies = movies,
                            lazyListState = romanceListState,
                            itemsTobeDisplayed = 3,
                            screenWidth = width,
                            onClick = { id ->
                                onClick(id)
                            }
                        )
                    }

                    documentaries?.takeIf { it.isNotEmpty() }?.let { movies ->
                        movieList(
                            animatedContentScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            genre = Genre.DOCUMENTARY,
                            movies = movies,
                            lazyListState = documentaryListState,
                            itemsTobeDisplayed = 3,
                            screenWidth = width,
                            onClick = { id ->
                                onClick(id)
                            }
                        )
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