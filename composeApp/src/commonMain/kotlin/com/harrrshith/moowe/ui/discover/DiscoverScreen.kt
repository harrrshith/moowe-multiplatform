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
import com.harrrshith.moowe.ui.discover.mock.mockMovies
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DiscoverRoute(
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
                    hazeState = hazeState,
                    trendingMovies = uiState.trendingMovies,
                    actionMovies = uiState.actionMovies,
                    adventureMovies = uiState.adventureMovies,
                    fantasyMovies = uiState.fantasyMovies,
                    documentaries = uiState.documentaries,
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
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}

@Composable
private fun DiscoverScreen(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    trendingMovies: List<Movie>? = null,
    actionMovies: List<Movie>? = null,
    adventureMovies: List<Movie>? = null,
    fantasyMovies: List<Movie>? = null,
    documentaries: List<Movie>? = null,
    onClick: (Int) -> Unit
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
                bottom = 120.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            trendingMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                trendingList(
                    movies = movies,
                    onClick = {id ->
                        onClick(id)
                    }
                )
            }

            actionMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                movieList(
                    genre = Genre.ACTION,
                    movies = movies,
                    lazyListState = actionListState,
                    itemsTobeDisplayed = 3,
                    screenWidth = width,
                    onClick = {id ->
                        onClick(id)
                    }
                )
            }

            adventureMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                movieList(
                    genre = Genre.ADVENTURE,
                    movies = movies,
                    lazyListState = adventureListState,
                    itemsTobeDisplayed = 3,
                    screenWidth = width,
                    onClick = {id ->
                        onClick(id)
                    }
                )
            }

            fantasyMovies?.takeIf { it.isNotEmpty() }?.let { movies ->
                movieList(
                    genre = Genre.FANTASY,
                    movies = movies,
                    lazyListState = romanceListState,
                    itemsTobeDisplayed = 3,
                    screenWidth = width,
                    onClick = {id ->
                        onClick(id)
                    }
                )
            }

            documentaries?.takeIf { it.isNotEmpty() }?.let { movies ->
                movieList(
                    genre = Genre.DOCUMENTARY,
                    movies = movies,
                    lazyListState = documentaryListState,
                    itemsTobeDisplayed = 3,
                    screenWidth = width,
                    onClick = {id ->
                        onClick(id)
                    }
                )
            }
        }
    }
}


@Preview()
@Composable
private fun DiscoverScreenPreview() {
    val fakeHazeState = rememberHazeState()
    DiscoverScreen(
        hazeState = fakeHazeState,
        trendingMovies = mockMovies,
        actionMovies = mockMovies,
        onClick = { }
    )
}