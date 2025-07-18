@file:OptIn(ExperimentalMaterial3Api::class)
package com.harrrshith.moowe.ui.discover

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.harrrshith.moowe.ui.discover.mock.mockMovies
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverRoute(
    viewModel: DiscoverViewModel = (koinViewModel())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hazeState = LocalHazeState.current
    Crossfade(uiState) {
        when(uiState.trendingLoading) {
            true -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .height(48.dp),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                }
            } else -> {
                DiscoverScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    hazeState = hazeState,
                    movies = uiState.trendingMovies
                )
            }
        }
    }

}

@Composable
private fun DiscoverScreen(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    movies: List<Movie>,
) {
    val width = screenWidth
    val actionListState = rememberLazyListState()
    val adventureListState = rememberLazyListState()
    val romanceListState = rememberLazyListState()
    val documentaryListState = rememberLazyListState()
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "moowe",
                hazeState = hazeState
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .hazeSource(state = hazeState),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            trendingList(
                movies = movies,
                onMovieClick = {}
            )

            movieList(
                genre = Genre.ACTION,
                movies = movies,
                lazyListState = actionListState,
                itemsTobeDisplayed = 3,
                screenWidth = width
            )

            movieList(
                genre = Genre.ADVENTURE,
                movies = movies,
                lazyListState = adventureListState,
                itemsTobeDisplayed = 3,
                screenWidth = width
            )

            movieList(
                genre = Genre.ROMANCE,
                movies = movies,
                lazyListState = romanceListState,
                itemsTobeDisplayed = 3,
                screenWidth = width
            )

            movieList(
                genre = Genre.DOCUMENTARY,
                movies = movies,
                lazyListState = documentaryListState,
                itemsTobeDisplayed = 3,
                screenWidth = width
            )
        }
    }
}


@Preview()
@Composable
private fun DiscoverScreenPreview() {
    val fakeHazeState = rememberHazeState()
    DiscoverScreen(
        hazeState = fakeHazeState,
        movies = mockMovies
    )
}